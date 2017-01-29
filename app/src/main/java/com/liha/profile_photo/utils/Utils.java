package com.liha.profile_photo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liha.profile_photo.constants.Constants;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    /**
     * Instance Retrofit NodeRed
     * @return retrofit builder
     */
    public static Retrofit getInstanceNodRedRetrofit(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(Constants.OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.OKHTTP_WRITE_TIMEOUT, TimeUnit.SECONDS);

        OkHttpClient client = httpClient.build();

        return new Retrofit.Builder()
                .baseUrl(Constants.getUploadUrlWS())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    /**
     * Encodage image in Base64
     * @param bm image Bipmap
     * @return encodedImage
     */
    public static String encodeBase64Image(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    /**
     * Encode a string in Base64
     * @param str string
     * @return string in base64
     */
    public static String encodeBase64String(String str) {

        byte[] data = new byte[0];
        try {
            data = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    /**
     * Image resize
     * @param ctx context
     * @param bitmap image
     * @return image rescaled
     */
    public static Bitmap resizeByWeight(Context ctx, Bitmap bitmap){
        int scaleMax = 100;
        Bitmap imageScaled = null;
        while (scaleMax != 0){
            Bitmap imageBitmap = Utils.scaleImage(ctx, bitmap, scaleMax);
            if (imageBitmap.getByteCount() < 25000000 ){
                imageScaled = imageBitmap;
                scaleMax = 0;
            } else {
                scaleMax = scaleMax - 5;
            }
        }
        return imageScaled;
    }

    /**
     * Redimensionnement d'une image
     * @param bmp image a resizer
     * @param scale pourcentage du redimensionnement
     * @return scaled
     */
    private static Bitmap scaleImage(Context ctx, Bitmap bmp, int scale){

        long nbBytes = bmp.getByteCount();

        Log.d(TAG, "scaleImage - nbBytes : " + nbBytes);

        // Technique 1
        Display display = getDisplaySize(ctx);
        Point size = new Point();
        display.getSize(size);
        int sizeY = bmp.getHeight() * scale / 100;
        int sizeX = bmp.getWidth() * sizeY / bmp.getHeight();

        Bitmap scaled = Bitmap.createScaledBitmap(bmp, sizeX, sizeY, false);

        long nbBytesResized = scaled.getByteCount();

        Log.d(TAG, "scaleImage - nbBytesResized : " + nbBytesResized);

        return scaled;
    }

    /**
     * Récuperation des dimensions de l'écran client
     * @param ctx contexte
     * @return display
     */
    private static Display getDisplaySize(Context ctx){
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay();
    }

    public static String hashStrSHA512(String strToHash){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(Constants.SALT.getBytes("UTF-8"));
            byte[] bytes = md.digest(strToHash.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b: bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            Log.e(TAG, "NoSuchAlgorithmException : " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException : " + e.getMessage());
        }
        return generatedPassword;
    }
}
