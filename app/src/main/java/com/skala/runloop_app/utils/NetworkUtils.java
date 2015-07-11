package com.skala.runloop_app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Skala
 */
public class NetworkUtils {

    public static Bitmap downloadImage(String stringURL) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(stringURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // download if ok
                InputStream in = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
