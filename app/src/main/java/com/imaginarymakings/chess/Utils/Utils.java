package com.imaginarymakings.chess.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.imaginarymakings.chess.Logic.Piece;
import com.imaginarymakings.chess.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rafaelfrancisco on 29/12/17.
 */

public class Utils {

    public static void setFloatable (Piece p, ImageView iv){
        switch (p){
            case PAWN_WHITE:
                iv.setImageResource(R.drawable.chess_pdt60);
                break;
            case PAWN_BLACK:
                iv.setImageResource(R.drawable.chess_plt60);
                break;
            case ROOK_WHITE:
                iv.setImageResource(R.drawable.chess_rdt60);
                break;
            case ROOK_BLACK:
                iv.setImageResource(R.drawable.chess_rlt60);
                break;
            case QUEEN_WHITE:
                iv.setImageResource(R.drawable.chess_qdt60);
                break;
            case QUEEN_BLACK:
                iv.setImageResource(R.drawable.chess_qlt60);
                break;
            case KING_WHITE:
                iv.setImageResource(R.drawable.chess_kdt60);
                break;
            case KING_BLACK:
                iv.setImageResource(R.drawable.chess_klt60);
                break;
            case KNIGHT_WHITE:
                iv.setImageResource(R.drawable.chess_ndt60);
                break;
            case KNIGHT_BLACK:
                iv.setImageResource(R.drawable.chess_nlt60);
                break;
            case BISHOP_WHITE:
                iv.setImageResource(R.drawable.chess_bdt60);
                break;
            case BISHOP_BLACK:
                iv.setImageResource(R.drawable.chess_blt60);
                break;
            case EMPTY:
                break;
        }
    }

    public static boolean haveNetworkConnection(Context c) {
        boolean haveConnectedWifi = false;

        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
        }
        return haveConnectedWifi;
    }

    public static boolean isValidIP(String ip){
        Pattern IP_ADDRESS
                = Pattern.compile(
                "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                        + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                        + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                        + "|[1-9][0-9]|[0-9]))");
        Matcher matcher = IP_ADDRESS.matcher(ip);

        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
