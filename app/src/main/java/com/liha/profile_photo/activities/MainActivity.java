package com.liha.profile_photo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.liha.profile_photo.R;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        loadProfileImage();

        Button buttonCameraBasic = (Button) findViewById(R.id.buttonPhotoCameraBasic);
        buttonCameraBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                startActivity(intent);
            }
        });
    }

    /** Set the toolbar */
    private void setToolbar(){
        // Set a Toolbar to replace the ActionBar.
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.activity_home_title);
        mToolbar.showOverflowMenu();
        setSupportActionBar(mToolbar);
    }

    /** load a image from an url with Picasso */
    private void loadProfileImage(){
        Log.d(TAG,"loadProfileImage");
        ImageView targetImageView = (ImageView) findViewById(R.id.imageProfileView);
        String internetUrl = "http://i.imgur.com/DvpvklR.png";
        Picasso.with(this).load(internetUrl).into(targetImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}

