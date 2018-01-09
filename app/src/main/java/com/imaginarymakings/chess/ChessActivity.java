package com.imaginarymakings.chess;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.imaginarymakings.chess.Logic.Piece;
import com.imaginarymakings.chess.Logic.SpaceAdapter;
import com.imaginarymakings.chess.Utils.Utils;

public class ChessActivity extends AppCompatActivity {

    private static final String TAG = "Chess Activity";

    GridView gv;
    ImageView overlay;
    TextView tv;

    private int movingPiece;

    private NetworkManager nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        overlay = findViewById(R.id.dragImage);
        gv = findViewById(R.id.chessGrid);
        tv = findViewById(R.id.editText);

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

                            if (nm == null)
                                doAIMove();

                            checkForGameLost();
                        }
                    }
                }

                return true;
            }
        });

        final SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        final Context c = this;

        Button reset = findViewById(R.id.deleteGame);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gv.setAdapter(new SpaceAdapter(c));

                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.remove("game");
                prefsEditor.apply();
            }
        });

        Gson gson = new Gson();
        String json = mPrefs.getString("game", "");
        GameInfo obj = gson.fromJson(json, GameInfo.class);

        if (obj != null)
            ((SpaceAdapter) gv.getAdapter()).setGameInfo(obj);

        LocalBroadcastManager.getInstance(this).registerReceiver(refreshReceived,
                new IntentFilter("refresh"));

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("mp")){
            setupMP();
        }
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

        nm = new NetworkManager(this, (SpaceAdapter) gv.getAdapter());

        ChooseDialog dialog = new ChooseDialog(this);
        dialog.show(getFragmentManager(), "Chess activity");
    }

    private BroadcastReceiver serverReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            nm.startServer();

            tv.setText(String.format(getString(R.string.your_ip), nm.getCurrentIP()));
            tv.setText(String.format("%s\n%s", tv.getText(), getString(R.string.waiting_conn)));
        }
    };

    private BroadcastReceiver clientReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String ipAddress = intent.getStringExtra("ip");
            nm.startClient(ipAddress);

            tv.setText(String.format(getString(R.string.your_ip), nm.getCurrentIP()));
            tv.setText(String.format("%s\n%s", tv.getText(), getString(R.string.waiting_conn)));
        }
    };

    private BroadcastReceiver refreshReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ((SpaceAdapter) gv.getAdapter()).notifyDataSetChanged();
            checkForGameLost();
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

        for (int i = 0; i < adapter.pieces.length; i++){
            if (adapter.pieces[i] == Piece.KING_WHITE)
                kingWhite = true;

            if (adapter.pieces[i] == Piece.KING_BLACK)
                kingBlack = true;
        }

        if (!kingBlack && !kingWhite){
            //Game finished
            gv.setEnabled(false);

            Toast.makeText(this, "Game ended", Toast.LENGTH_LONG).show();
        }
    }
}