package com.imaginarymakings.chess.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.widget.ImageView;

import com.imaginarymakings.chess.Logic.Piece;
import com.imaginarymakings.chess.R;

import java.io.ByteArrayOutputStream;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rafaelfrancisco on 29/12/17.
 */

public class Utils {

    public static void setFloatable (Piece p, ImageView iv){
        switch (p){
            case PAWN_WHITE:
                iv.setImageResource(R.drawable.chess_plt60);
                break;
            case PAWN_BLACK:
                iv.setImageResource(R.drawable.chess_pdt60);
                break;
            case ROOK_WHITE:
                iv.setImageResource(R.drawable.chess_rlt60);
                break;
            case ROOK_BLACK:
                iv.setImageResource(R.drawable.chess_rdt60);
                break;
            case QUEEN_WHITE:
                iv.setImageResource(R.drawable.chess_qlt60);
                break;
            case QUEEN_BLACK:
                iv.setImageResource(R.drawable.chess_qdt60);
                break;
            case KING_WHITE:
                iv.setImageResource(R.drawable.chess_klt60);
                break;
            case KING_BLACK:
                iv.setImageResource(R.drawable.chess_kdt60);
                break;
            case KNIGHT_WHITE:
                iv.setImageResource(R.drawable.chess_nlt60);
                break;
            case KNIGHT_BLACK:
                iv.setImageResource(R.drawable.chess_ndt60);
                break;
            case BISHOP_WHITE:
                iv.setImageResource(R.drawable.chess_blt60);
                break;
            case BISHOP_BLACK:
                iv.setImageResource(R.drawable.chess_bdt60);
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

    public static int randInt(int min, int max) {
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static boolean isIntInArray(int[] array, int number){
        for (int anArray : array) {
            if (number == anArray) {
                return true;
            }
        }

        return false;
    }

    public static Bitmap convert(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}