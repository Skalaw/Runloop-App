package com.skala.runloop_app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skala.runloop_app.utils.MemCache;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Skala
 */
public class MemberAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;

    private ArrayList<MemberModel> mMemberList;
    private Resources mResources;

    public MemberAdapter(Context context, ArrayList<MemberModel> memberList) {
        mLayoutInflater = LayoutInflater.from(context);
        mMemberList = memberList;
        mResources = context.getResources();
    }

    @Override
    public int getCount() {
        return mMemberList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMemberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_member_list, parent, false);

            holder = new ViewHolder();
            holder.photo = (ImageView) convertView.findViewById(R.id.memberPhoto);
            holder.fullname = (TextView) convertView.findViewById(R.id.memberFullname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.position = position;

        MemberModel memberModel = (MemberModel) getItem(position);

        holder.fullname.setText(memberModel.getFullName());
        loadAvatar(memberModel, holder, position);

        return convertView;
    }

    private void loadAvatar(MemberModel memberModel, final ViewHolder holder, final int position) {
        holder.photo.setImageResource(R.drawable.ic_image_grey_600_24dp); // mockup

        final String stringURL = memberModel.getImageURL();

        new AsyncTask<Void, Void, Drawable>() {

            @Override
            protected Drawable doInBackground(Void... params) {
                Bitmap bitmap = MemCache.get(stringURL);
                if (bitmap == null) {
                    bitmap = downloadImage(stringURL);
                    if (bitmap != null) {
                        MemCache.put(stringURL, bitmap);
                    }
                }

                if (holder.position != position) { // if ViewHolder disagrees with position - reject operations
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
                if (holder.position == position && drawable != null) { // set drawable only when drawable is correct position with holder
                    holder.photo.setImageDrawable(drawable);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private Bitmap downloadImage(String stringURL) {
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

    private Drawable adjustAvatar(Bitmap bitmap) {
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

    static class ViewHolder {
        ImageView photo;
        TextView fullname;
        int position;
    }
}
