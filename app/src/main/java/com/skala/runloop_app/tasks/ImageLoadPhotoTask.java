package com.skala.runloop_app.tasks;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.skala.runloop_app.utils.MemCache;
import com.skala.runloop_app.utils.NetworkUtils;
import com.skala.runloop_app.utils.Utility;

import java.lang.ref.WeakReference;

/**
 * @author Skala
 */
public class ImageLoadPhotoTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> mImageViewRef;

    public ImageLoadPhotoTask(ImageView imageViewRef) {
        mImageViewRef = new WeakReference<>(imageViewRef);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String stringURL = params[0];
        Bitmap bitmap = MemCache.get(stringURL);
        if (bitmap == null) {
            bitmap = NetworkUtils.downloadImage(stringURL);
            if (bitmap != null) {
                MemCache.put(stringURL, bitmap);
            }
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView imageView = mImageViewRef.get();
        if (imageView != null && bitmap != null) {
            final Drawable drawable = new BitmapDrawable(imageView.getResources(), bitmap);
            imageView.setImageDrawable(drawable);
            Utility.animationLoadImage(drawable, 1500);
        }
    }
}
