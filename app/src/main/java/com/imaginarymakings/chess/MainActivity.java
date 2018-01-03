package com.imaginarymakings.chess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.imaginarymakings.chess.Utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        Button sp = findViewById(R.id.sp_button);
        Button mp = findViewById(R.id.mp_button);

        sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChessActivity.class);
                startActivity(intent);
            }
        });

        mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.haveNetworkConnection(context)){
                    Intent intent = new Intent(context, ChessActivity.class);
                    intent.putExtra("mp", true);

                    startActivity(intent);
                } else {
                    Snackbar mySnackbar = Snackbar.make(view, R.string.no_connection, Snackbar.LENGTH_LONG);
                    mySnackbar.show();
                }
            }
        });
    }
}
