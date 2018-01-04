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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.imaginarymakings.chess.R;
import com.imaginarymakings.chess.Utils.Utils;

/**
 * Created by rafaelfrancisco on 04/01/18.
 */

@SuppressLint("ValidFragment")
public class AddressDialog extends DialogFragment {

    private Context c;

    public AddressDialog(Context c) {
        setCancelable(false);
        this.c = c;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertLayout = View.inflate(c, R.layout.address_dialog, null);

        builder.setView(alertLayout);
        builder.setMessage(R.string.choose_ip)
                .setPositiveButton(R.string.confirm_ip, null);

        final AlertDialog d = builder.create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText text = alertLayout.findViewById(R.id.addressIP);

                        if (Utils.isValidIP(text.getText().toString())){
                            Intent intent = new Intent("client");
                            intent.putExtra("ip", text.getText().toString());

                            LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
                            dismiss();
                        } else {
                            text.setText("");
                            text.setHint(R.string.invalid_ip);
                        }
                    }
                });
            }
        });

        return d;
    }
}
