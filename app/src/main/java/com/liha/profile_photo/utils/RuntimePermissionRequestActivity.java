package com.liha.profile_photo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.liha.profile_photo.activities.MainActivity;

public class RuntimePermissionRequestActivity extends Activity {

    private static final String TAG = RuntimePermissionRequestActivity.class.getName();
    private static final int PERMISSIONS_REQUEST_CAMERA = 1;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        } else {
            startCamActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamActivity();
                } else {
                    Toast.makeText(this, "Sorry, Please grant camera permission.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void startCamActivity(){

        Log.d(TAG, "startCamActivity");
        Intent intent = new Intent(this, MainActivity.class);

        this.startActivity(intent);
        ActivityCompat.finishAfterTransition(this);

    }
}
