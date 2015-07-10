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
        downloadImage(memberModel, holder, position);

        return convertView;
    }

    private void downloadImage(final MemberModel memberModel, final ViewHolder holder, final int position) {
        holder.photo.setImageResource(R.drawable.ic_image_grey_600_24dp); // mockup

        new AsyncTask<Void, Void, Drawable>() {

            @Override
            protected Drawable doInBackground(Void... params) {
                String stringURL = memberModel.getImageURL();

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

                if (bitmap == null) {
                    return null;
                }

                int sizeAvatar = (int) mResources.getDimension(R.dimen.list_size_avatar);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getWidth()); // cut top image

                if (sizeAvatar < bitmap.getWidth()) { // we don't want resize up bitmap
                    bitmap = Bitmap.createScaledBitmap(bitmap, sizeAvatar, sizeAvatar, true); // resize to small bitmap
                }

                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(mResources, bitmap);
                drawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);

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

    static class ViewHolder {
        ImageView photo;
        TextView fullname;
        int position;
    }
}
