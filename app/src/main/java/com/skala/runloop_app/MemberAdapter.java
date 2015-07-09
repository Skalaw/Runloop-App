package com.skala.runloop_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        MemberModel memberModel = (MemberModel) getItem(position);
        holder.fullname.setText(memberModel.getFullName());


        return convertView;
    }

    static class ViewHolder {
        ImageView photo;
        TextView fullname;
    }
}
