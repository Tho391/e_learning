package com.example.dardan.elearning;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class Ulti {
    private static final String SHARE_PREFERENCES = "sharePreferences";

    public static Uri getUrl(int res) {
        return Uri.parse("android.resource://com.example.dardan.elearning/" + res);
    }

    public static void CreateDefaultData(Context context) {

    }

    public void CreateDirectory(String name) {
        File file = new File(name);
        file.mkdir();
    }

    public void saveDataDefault(Context context) {
        String fileName = "thangcoder.com";
        String content = "Blog chia se kien thuc lap trinh";

        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readData(Context context) {
        try {
            FileInputStream in = context.openFileInput("thangcoder.com");

            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }


        } catch (Exception e) {

        }
    }

    public static byte[] BitmapToByte(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap ByteToBitmap(byte[] byteArray) {
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bmp;
    }

    public static void putToSharePreferences(Context context, String objectName, Object object) {
        SharedPreferences mPrefs = context.getSharedPreferences(SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        prefsEditor.putString(objectName, json);
        prefsEditor.apply();
    }

    public static Category getDataFromSharePreferences(Context context, String objectName) {
        SharedPreferences mPrefs = context.getSharedPreferences(SHARE_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(objectName, "");
        Category obj = gson.fromJson(json, Category.class);
        return obj;
    }
}
