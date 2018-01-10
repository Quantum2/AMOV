package com.imaginarymakings.chess.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.imaginarymakings.chess.Dialogs.ChooseDialog;
import com.imaginarymakings.chess.Logic.ChessMoves;
import com.imaginarymakings.chess.Logic.GameInfo;
import com.imaginarymakings.chess.Logic.PastGame;
import com.imaginarymakings.chess.Logic.Piece;
import com.imaginarymakings.chess.Logic.Player;
import com.imaginarymakings.chess.Logic.Profile;
import com.imaginarymakings.chess.Logic.SpaceAdapter;
import com.imaginarymakings.chess.R;
import com.imaginarymakings.chess.Utils.NetworkManager;
import com.imaginarymakings.chess.Utils.Utils;

public class ChessActivity extends AppCompatActivity {

    private static final String TAG = "Chess Activity";

    GridView gv;
    ImageView overlay;
    TextView tv;

    TextView localName;
    TextView opponentName;

    ImageView localProfile;
    ImageView opponentProfile;

    ImageView currentColor;
    TextView currentColorText;

    private int movingPiece;
    private PastGame game;

    private NetworkManager nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        final Context c = this;
        game = new PastGame();

        overlay = findViewById(R.id.dragImage);
        gv = findViewById(R.id.chessGrid);
        tv = findViewById(R.id.editText);

        localName = findViewById(R.id.nameLocal);
        opponentName = findViewById(R.id.nameOpponent);

        localProfile = findViewById(R.id.imageLocal);
        opponentProfile = findViewById(R.id.imageOpponent);

        currentColor = findViewById(R.id.turn);
        currentColorText = findViewById(R.id.colorDesc);

        gv.setAdapter(new SpaceAdapter(this));
        gv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();
                float currentXPosition = motionEvent.getX();
                float currentYPosition = motionEvent.getY();

                int position = gv.pointToPosition((int) currentXPosition, (int) currentYPosition);
                if (action == MotionEvent.ACTION_DOWN && position != -1 && ((SpaceAdapter) gv.getAdapter()).pieces[position] != Piece.EMPTY) {
                    overlay.setX(currentXPosition);
                    overlay.setY(currentYPosition);

                    movingPiece = position;
                    Utils.setFloatable(((SpaceAdapter) gv.getAdapter()).pieces[position], overlay);
                    overlay.setVisibility(View.VISIBLE);
                }

                if (action == MotionEvent.ACTION_MOVE){
                    overlay.setX(currentXPosition);
                    overlay.setY(currentYPosition);
                }

                if (action == MotionEvent.ACTION_UP ){
                    overlay.setVisibility(View.INVISIBLE);

                    if (position != -1){
                        if (ChessMoves.movePiece(movingPiece, position, ((SpaceAdapter) gv.getAdapter()), false)){
                            ((SpaceAdapter) gv.getAdapter()).notifyDataSetChanged();
                            Utils.addTextToTicker(tv, Utils.getPlayerLocalizedName(((SpaceAdapter) gv.getAdapter()).currentPlayer, c) + ": " + ((SpaceAdapter) gv.getAdapter()).lastMessage);

                            if (nm == null){
                                doAIMove();
                                Utils.addTextToTicker(tv, Utils.getPlayerLocalizedName(((SpaceAdapter) gv.getAdapter()).currentPlayer, c) + ": " + ((SpaceAdapter) gv.getAdapter()).lastMessage);
                            } else{
                                GameInfo gm = ((SpaceAdapter) gv.getAdapter()).getGameInfo();
                                Profile profile = Profile.getLoadedProfile(c);
                                if (profile != null)
                                    gm.profile = profile;

                                nm.gameInfo = gm;
                            }

                            game.addMovement(movingPiece, position);
                            checkForGameLost();
                        } else {
                            Toast.makeText(c, R.string.invalid_move, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                return true;
            }
        });

        final SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);

        Button reset = findViewById(R.id.deleteGame);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gv.setAdapter(new SpaceAdapter(c));

                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.remove("game");
                prefsEditor.apply();

                gv.setEnabled(true);
                tv.setText("");
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(refreshReceived,
                new IntentFilter("refresh"));

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("mp")){
            gv.setEnabled(false);
            setupMP();
        } else {
            Gson gson = new Gson();
            String json = mPrefs.getString("game", "");
            GameInfo obj = gson.fromJson(json, GameInfo.class);

            if (obj != null)
                ((SpaceAdapter) gv.getAdapter()).setGameInfo(obj);

            opponentName.setText(R.string.ai_profile_name);
        }

        Profile profile = Profile.getLoadedProfile(this);
        if (profile == null)
            localName.setText(R.string.no_profile);
        else {
            localName.setText(profile.name);

            if (profile.photo != null)
                localProfile.setImageBitmap(Utils.convert(profile.photo));
        }

        checkForGameLost();
    }

    @Override
    public void onBackPressed() {
        if (nm != null)
            nm.endGame();

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(((SpaceAdapter) gv.getAdapter()).getGameInfo());
        prefsEditor.putString("game", json);
        prefsEditor.apply();

        super.onStop();
    }

    protected void setupMP() {
        LocalBroadcastManager.getInstance(this).registerReceiver(serverReceived,
                new IntentFilter("server"));

        LocalBroadcastManager.getInstance(this).registerReceiver(clientReceived,
                new IntentFilter("client"));

        LocalBroadcastManager.getInstance(this).registerReceiver(connected,
                new IntentFilter("connected"));

        nm = new NetworkManager(this, (SpaceAdapter) gv.getAdapter());

        ChooseDialog dialog = new ChooseDialog(this);
        dialog.show(getFragmentManager(), "Chess activity");
    }

    private BroadcastReceiver serverReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            nm.startServer();
            tv.setText(String.format(getString(R.string.your_ip), nm.getCurrentIP()));
        }
    };

    private BroadcastReceiver clientReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String ipAddress = intent.getStringExtra("ip");
            nm.startClient(ipAddress);

            tv.setText(R.string.connecting);
        }
    };

    private BroadcastReceiver refreshReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            GameInfo gameInfo = (GameInfo) intent.getSerializableExtra("gameInfo");
            if (gameInfo != null){
                Utils.addTextToTicker(tv, Utils.getPlayerLocalizedName(((SpaceAdapter) gv.getAdapter()).currentPlayer, context) + ": " + gameInfo.message);

                ((SpaceAdapter) gv.getAdapter()).setGameInfo(gameInfo);

                if (gameInfo.profile != null){
                    if (gameInfo.profile.name != null)
                        opponentName.setText(gameInfo.profile.name);
                    else
                        opponentName.setText(R.string.no_profile);

                    if (gameInfo.profile.photo != null)
                        opponentProfile.setImageBitmap(Utils.convert(gameInfo.profile.photo));
                }

                if (gameInfo.turn == -1){
                    gv.setAdapter(new SpaceAdapter(context));

                    if (!nm.isServer)
                        ((SpaceAdapter) gv.getAdapter()).whoAmI = Player.BLACK;
                }
            }

            ((SpaceAdapter) gv.getAdapter()).notifyDataSetChanged();
            checkForGameLost();
        }
    };

    private BroadcastReceiver connected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            gv.setEnabled(true);
            Utils.addTextToTicker(tv, "Connected");

            GameInfo gm = new GameInfo();
            Profile profile = Profile.getLoadedProfile(context);
            if (profile != null)
                gm.profile = profile;

            nm.gameInfo = gm;
        }
    };

    private void doAIMove(){
        boolean done;
        SpaceAdapter adapter = (SpaceAdapter) gv.getAdapter();
        int piece, positionTo;

        do {
            do {
                piece = Utils.randInt(0, 63);
            }while (adapter.pieces[piece] == Piece.EMPTY);

            do {
                positionTo = Utils.randInt(0, 63);
            }while (positionTo == piece);

            done = ChessMoves.movePiece(piece, positionTo, adapter, true);
        }while (!done);

        Log.d(TAG, "AI moved piece");
    }

    private void checkForGameLost(){
        SpaceAdapter adapter = (SpaceAdapter) gv.getAdapter();
        boolean kingBlack = false, kingWhite = false;

        if (adapter.currentPlayer == Player.WHITE){
            currentColor.setImageResource(R.drawable.chess_blt60);
            currentColorText.setText(R.string.whites);
        } else {
            currentColor.setImageResource(R.drawable.chess_bdt60);
            currentColorText.setText(R.string.blacks);
        }

        for (int i = 0; i < adapter.pieces.length; i++){
            if (adapter.pieces[i] == Piece.KING_WHITE)
                kingWhite = true;

            if (adapter.pieces[i] == Piece.KING_BLACK)
                kingBlack = true;
        }

        if (!kingBlack || !kingWhite){
            //Game finished
            gv.setEnabled(false);
            Toast.makeText(this, "Game ended", Toast.LENGTH_LONG).show();

            Profile profile = Profile.getLoadedProfile(this);
            if (profile == null){
                Toast.makeText(this, "No active profile, so no stats were saved", Toast.LENGTH_LONG).show();
                return;
            }

            if (adapter.whoAmI == Player.WHITE && kingWhite){
                game.won = true;
                profile.wonGames++;
            } else {
                profile.lostGames++;
            }

            if (!kingBlack){
                Utils.addTextToTicker(tv, Utils.getPlayerLocalizedName(Player.WHITE, this) + getString(R.string.player_won));
            } else {
                Utils.addTextToTicker(tv, Utils.getPlayerLocalizedName(Player.BLACK, this) + getString(R.string.player_won));
            }

            profile.saveProfileToSharedPreferences(this);
        }
    }
}