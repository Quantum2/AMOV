package com.imaginarymakings.chess;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.imaginarymakings.chess.Logic.Profile;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    Profile playerProfile;

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        iv = findViewById(R.id.editPhoto);
        playerProfile = Profile.getLoadedProfile(this);

        if (playerProfile == null){
            playerProfile = new Profile();
            playerProfile.saveProfileToSharedPreferences(this);
        } else {
            if (playerProfile.name != null){
                EditText ed = findViewById(R.id.editName);
                ed.setText(playerProfile.name);
            }

            if (playerProfile.photo != null){
                iv.setImageDrawable(playerProfile.photo);
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
    }

    @Override
    protected void onStop() {
        playerProfile.saveProfileToSharedPreferences(this);
        super.onStop();
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
            playerProfile.photo = new BitmapDrawable(getResources(), imageBitmap);
            playerProfile.saveProfileToSharedPreferences(this);
        }
    }
}