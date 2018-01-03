package com.imaginarymakings.chess;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by rafaelfrancisco on 02/01/18.
 */

@SuppressLint("ValidFragment")
public class ConnectDialog extends DialogFragment {

    private Context c;

    public ConnectDialog(Context context) {
        setCancelable(false);
        c = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.ai_or_people)
                .setPositiveButton(R.string.ai, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent("ai");
                        LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
                    }
                })
                .setNegativeButton(R.string.people, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent("player");
                        LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
