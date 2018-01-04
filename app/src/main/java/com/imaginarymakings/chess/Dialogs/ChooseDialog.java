package com.imaginarymakings.chess.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.imaginarymakings.chess.R;

/**
 * Created by rafaelfrancisco on 04/01/18.
 */

@SuppressLint("ValidFragment")
public class ChooseDialog extends DialogFragment {

    private Context c;

    public ChooseDialog(Context context) {
        c = context;
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.server_or_client)
                .setPositiveButton(R.string.server, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent("server");
                        LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
                    }
                })
                .setNegativeButton(R.string.client, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddressDialog cDialog = new AddressDialog(c);
                        cDialog.show(getFragmentManager(), "Chess activity");
                        dismiss();
                    }
                });

        return builder.create();
    }
}
