package com.imaginarymakings.chess.Logic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by rafaelfrancisco on 08/01/18.
 */

public class Profile implements Serializable {
    public String name;

    public String photo;

    public int wonGames;
    public int lostGames;

    public Profile() {
        wonGames = 0;
        lostGames = 0;
    }

    public static Profile getLoadedProfile(Context c){
        SharedPreferences sharedPref = ((Activity) c).getPreferences(Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString("Profile", "");
        return gson.fromJson(json, Profile.class);
    }

    public void saveProfileToSharedPreferences(Context c){
        SharedPreferences sharedPref = ((Activity) c).getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this);
        prefsEditor.putString("Profile", json);
        prefsEditor.apply();
    }

    public static void deleteProfile(Context c){
        SharedPreferences sharedPref = ((Activity) c).getPreferences(Context.MODE_PRIVATE);

        sharedPref.edit().remove("Profile").apply();
    }
}
