package com.liha.profile_photo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Surface;

import com.liha.profile_photo.R;
import com.liha.profile_photo.utils.Utils;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = PhotoActivity.class.getName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;

    /**
     * Chargement de l'activite
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dispatchTakePictureIntent();
    }

    /**
     * Ouverture de la preview du SDK
     * Gestion de la prise de photo par le SDK
     */
    private void dispatchTakePictureIntent() {

        try {
            Log.d(TAG, "Ouverture de la preview du SDK");

            Intent pickIntent = new Intent();
            pickIntent.setType("image/*");
            pickIntent.setAction(Intent.ACTION_GET_CONTENT);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
            Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
            chooserIntent.putExtra(
                    Intent.EXTRA_INITIAL_INTENTS,
                    new Intent[] { takePictureIntent }
            );

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = createImageFile();
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.liha.profile_photo.fileprovider",
                            photoFile);
                    // takePictureIntent.putExtra("photo", photoURI);
                    // setResult(RESULT_OK, takePictureIntent);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creation du fichier temporaire
     * Gestion de la prise de photo par le SDK
     * @return image
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        Log.d(TAG, "Creation du fichier image");
        // Create an image file name
        // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
        String imageFileName = Utils.hashStrSHA512("123456");
        // String imageFileName = timeStamp + "_";
        // File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = this.getExternalCacheDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Log.d(TAG, "Fichier image cree : " + image.getAbsolutePath());
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Méthode une fois la photo prise avec succès
     * Gestion de la prise de photo par le SDK
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                File f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                    if (bitmap != null) {
                        Bitmap imageBitmap = Utils.resizeByWeight(this, bitmap);
                    } else {
                        Uri imageUri = data.getData();
                        Bitmap bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    }
                } catch (IOException e) {
                    Log.d(TAG, "[Error] onActivityResult - erreur de récupération de l'image générée");
                }
                // sendMessage(Utils.encodeBase64Image(imageBitmap));
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    /**
     * Rotation de l'image en fonction de son orientation
     * @param bitmap image
     * @return bitmap image
     */
    private Bitmap setRotation(Bitmap bitmap){
        Display display = getWindowManager().getDefaultDisplay();
        int rotation = 0;
        switch (display.getRotation()) {
            case Surface.ROTATION_0: // This is display orientation
                rotation = 90;
                break;
            case Surface.ROTATION_90:
                rotation = 0;
                break;
            case Surface.ROTATION_180:
                rotation = 270;
                break;
            case Surface.ROTATION_270:
                rotation = 180;
                break;
        }

        bitmap = rotateImage(bitmap, rotation);

        return bitmap;
    }

    /**
     * Image rotation according to an angle
     * @param source image
     * @param angle angle
     * @return bitmap
     */
    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }
}
