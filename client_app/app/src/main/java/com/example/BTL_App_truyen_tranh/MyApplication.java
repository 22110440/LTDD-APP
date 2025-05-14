package com.example.BTL_App_truyen_tranh;

import android.content.Context;
import android.content.SharedPreferences;
import android.app.Application;
import android.graphics.Bitmap;
import android.util.Base64;

import com.example.BTL_App_truyen_tranh.utils.Constants;

import java.io.ByteArrayOutputStream;

public class MyApplication extends Application {
    private static SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME = "MyPrefs";

    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return "data:image/png;base64," + base64Image;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.ACCESS_TOKEN, token);
        editor.apply();
    }

    public static String getToken() {
        return sharedPreferences.getString(Constants.ACCESS_TOKEN, null);
    }
}
