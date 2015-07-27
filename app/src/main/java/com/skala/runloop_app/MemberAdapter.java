package com.skala.runloop_app;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skala.runloop_app.models.MemberModel;
import com.skala.runloop_app.tasks.ImageLoadAvatarTask;

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
        MemberViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_member_list, parent, false);

            holder = new MemberViewHolder();
            holder.photo = (ImageView) convertView.findViewById(R.id.memberPhoto);
            holder.fullName = (TextView) convertView.findViewById(R.id.memberFullname);
            convertView.setTag(holder);
        } else {
            holder = (MemberViewHolder) convertView.getTag();
        }

        holder.position = position;

        MemberModel memberModel = (MemberModel) getItem(position);

        holder.fullName.setText(memberModel.getFullName());
        loadAvatar(memberModel.getImageURL(), holder, position);

        return convertView;
    }

    private void loadAvatar(String stringURL, MemberViewHolder holder, int position) {
        holder.photo.setImageResource(R.drawable.ic_image_grey_600_24dp); // mock-up

        ImageLoadAvatarTask imageLoadAvatarTask = new ImageLoadAvatarTask(holder, position, mResources);
        imageLoadAvatarTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, stringURL);
    }
}
