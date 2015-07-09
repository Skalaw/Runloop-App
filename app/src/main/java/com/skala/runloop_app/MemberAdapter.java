package com.skala.runloop_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

    public MemberAdapter(Context context, ArrayList<MemberModel> memberList) {
        mLayoutInflater = LayoutInflater.from(context);
        mMemberList = memberList;
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
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
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

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (holder.position == position && bitmap != null) { // set bitmap only when bitmap is correct position with holder
                    holder.photo.setImageBitmap(bitmap);
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
