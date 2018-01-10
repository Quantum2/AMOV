package com.a21230528.chess.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.a21230528.chess.Logic.Profile;
import com.a21230528.chess.R;
import com.a21230528.chess.Utils.Utils;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    Profile playerProfile;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        EditText ed = findViewById(R.id.editName);
        TextView won = findViewById(R.id.wonGames);
        TextView lost = findViewById(R.id.lostGames);

        iv = findViewById(R.id.editPhoto);
        playerProfile = Profile.getLoadedProfile(this);

        if (playerProfile == null){
            playerProfile = new Profile();
            playerProfile.saveProfileToSharedPreferences(this);
        } else {
            if (playerProfile.name != null){
                ed.setText(playerProfile.name);
            }

            if (playerProfile.photo != null){
                iv.setImageBitmap(Utils.convert(playerProfile.photo));
            }
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                    dispatchTakePictureIntent();
                }
            }
        });

        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0){
                    playerProfile.name = charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        won.setText(getString(R.string.won_label) + playerProfile.wonGames);
        lost.setText(getString(R.string.lost_label) + playerProfile.lostGames);
    }

    @Override
    protected void onPause() {
        if (playerProfile.name.length() > 0)
            playerProfile.saveProfileToSharedPreferences(this);

        super.onPause();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            iv.setImageBitmap(imageBitmap);
            playerProfile.photo = Utils.convert(imageBitmap);
            playerProfile.saveProfileToSharedPreferences(this);
        }
    }
}