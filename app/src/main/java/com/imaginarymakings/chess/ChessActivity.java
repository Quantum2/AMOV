package com.imaginarymakings.chess;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.imaginarymakings.chess.Logic.ChessMoves;
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
        final SpaceAdapter adapter = new SpaceAdapter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("mp")){
            setupMP();
        }

        gv.setAdapter(adapter);
        gv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();
                float currentXPosition = motionEvent.getX();
                float currentYPosition = motionEvent.getY();

                int position = gv.pointToPosition((int) currentXPosition, (int) currentYPosition);
                if (action == MotionEvent.ACTION_DOWN && position != -1 && adapter.pieces[position] != Piece.EMPTY) {
                    overlay.setX(currentXPosition);
                    overlay.setY(currentYPosition);

                    movingPiece = position;
                    Utils.setFloatable(adapter.pieces[position], overlay);
                    overlay.setVisibility(View.VISIBLE);
                }

                if (action == MotionEvent.ACTION_MOVE){
                    overlay.setX(currentXPosition);
                    overlay.setY(currentYPosition);
                }

                if (action == MotionEvent.ACTION_UP ){
                    overlay.setVisibility(View.INVISIBLE);

                    if (position != -1){
                        ChessMoves.movePiece(movingPiece, position, adapter.pieces);
                        adapter.notifyDataSetChanged();
                    }
                }

                return true;
            }
        });
    }

    protected void setupMP() {
        LocalBroadcastManager.getInstance(this).registerReceiver(aiReceiver,
                new IntentFilter("ai"));

        LocalBroadcastManager.getInstance(this).registerReceiver(playerReceiver,
                new IntentFilter("player"));

        nm = new NetworkManager(this);

        ConnectDialog dialog = new ConnectDialog(this);
        dialog.show(getFragmentManager(), "Chess activity");
    }

    private BroadcastReceiver aiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    private BroadcastReceiver playerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            nm.startServer();

            tv.setText(String.format(getString(R.string.your_ip), nm.getCurrentIP()));
            tv.setText(String.format("%s\n%s", tv.getText(), getString(R.string.waiting_conn)));
        }
    };
}