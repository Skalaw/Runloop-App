package com.skala.runloop_app.tasks;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.skala.runloop_app.MemberViewHolder;
import com.skala.runloop_app.R;
import com.skala.runloop_app.utils.MemCache;
import com.skala.runloop_app.utils.NetworkUtils;
import com.skala.runloop_app.utils.Utility;

/**
 * @author Skala
 */
public class ImageLoadAvatarTask extends AsyncTask<String, Void, Drawable> {
    private final MemberViewHolder mViewHolder;
    private final int mPosition;
    private final Resources mResources;

    public ImageLoadAvatarTask(MemberViewHolder viewHolder, int position, Resources resources) {
        mViewHolder = viewHolder;
        mPosition = position;
        mResources = resources;
    }

    @Override
    protected Drawable doInBackground(String... params) {
        String stringURL = params[0];

        Bitmap bitmap = MemCache.get(stringURL);
        if (bitmap == null) {
            bitmap = NetworkUtils.downloadImage(stringURL);
            if (bitmap != null) {
                MemCache.put(stringURL, bitmap);
            }
        }

        if (mViewHolder.position != mPosition) { // if ViewHolder disagrees with position - reject operations
            return null;
        }

        Drawable drawable = null;
        if (bitmap != null) {
            drawable = adjustAvatar(bitmap);
        }

        return drawable;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        if (mViewHolder != null && mViewHolder.position == mPosition && drawable != null) { // set drawable only when drawable is correct position with holder
            mViewHolder.photo.setImageDrawable(drawable);
            Utility.animationLoadImage(drawable, 400);
        }
    }

    private Drawable adjustAvatar(Bitmap bitmap) {
        if (mResources == null) {
            return null;
        }

        int sizeAvatar = (int) mResources.getDimension(R.dimen.list_size_avatar);

        int bitmapSize = Math.min(bitmap.getWidth(), bitmap.getHeight()); // protection - get smallest size from rectangle (and cut to square)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapSize, bitmapSize); // cut image from top or left

        if (sizeAvatar < bitmapSize) { // we don't want resize up bitmap
            bitmapSize = sizeAvatar;
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmapSize, bitmapSize, true); // resize to smallest bitmap
        }

        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(mResources, bitmap);
        drawable.setCornerRadius(bitmapSize / 2);

        return drawable;
    }
}
