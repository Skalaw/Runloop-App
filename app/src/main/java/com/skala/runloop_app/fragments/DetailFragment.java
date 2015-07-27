package com.skala.runloop_app.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skala.runloop_app.DetailActivity;
import com.skala.runloop_app.R;
import com.skala.runloop_app.models.MemberModel;
import com.skala.runloop_app.receivers.NetworkChangeReceiver;
import com.skala.runloop_app.tasks.ImageLoadPhotoTask;
import com.skala.runloop_app.utils.FontUtils;

/**
 * @author Skala
 */
public class DetailFragment extends Fragment {
    private MemberModel mMemberModel;

    private ImageView mPhotoView;
    private TextView mFullNameView;
    private TextView mPositionView;
    private TextView mDescriptionView;

    private ImageLoadPhotoTask mImageLoadPhotoTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMemberModel = arguments.getParcelable(DetailActivity.MEMBER_KEY);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mPhotoView = (ImageView) rootView.findViewById(R.id.memberPhoto);
        mFullNameView = (TextView) rootView.findViewById(R.id.memberFullname);
        mPositionView = (TextView) rootView.findViewById(R.id.memberPosition);
        mDescriptionView = (TextView) rootView.findViewById(R.id.memberDescription);

        Typeface typeface = FontUtils.getTypeface(getActivity().getAssets(), FontUtils.FONT_TYPE_ROBOTO_MEDIUM);
        mPositionView.setTypeface(typeface);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadViews();
    }

    private void loadViews() {
        if (mMemberModel == null) {
            return;
        }

        mFullNameView.setText(mMemberModel.getFullName());
        mPositionView.setText(mMemberModel.getPosition());
        mDescriptionView.setText(mMemberModel.getDescription());

        loadPhoto();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mNetworkChangeReceiver, new IntentFilter(NetworkChangeReceiver.INTERNET_IS_CONNECTED));
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mNetworkChangeReceiver);
        super.onPause();
    }

    private BroadcastReceiver mNetworkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(NetworkChangeReceiver.INTERNET_IS_CONNECTED)) {
                if (mPhotoView.getDrawable() == null && !isTaskImageIsRunning()) { // bitmap is not load
                    loadPhoto();
                }
            }
        }
    };

    private void loadPhoto() {
        mImageLoadPhotoTask = new ImageLoadPhotoTask(mPhotoView);
        mImageLoadPhotoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMemberModel.getImageURL());
    }

    private boolean isTaskImageIsRunning() {
        return mImageLoadPhotoTask != null && mImageLoadPhotoTask.getStatus() != AsyncTask.Status.FINISHED;

    }
}
