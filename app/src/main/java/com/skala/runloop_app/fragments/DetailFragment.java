package com.skala.runloop_app.fragments;

import android.graphics.Bitmap;
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
import com.skala.runloop_app.MemberModel;
import com.skala.runloop_app.R;
import com.skala.runloop_app.utils.MemCache;
import com.skala.runloop_app.utils.NetworkUtils;

/**
 * @author Skala
 */
public class DetailFragment extends Fragment {
    private MemberModel mMemberModel;

    private ImageView mPhotoView;
    private TextView mFullNameView;
    private TextView mPositionView;
    private TextView mDescriptionView;

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

        loadPhoto(mMemberModel.getImageURL());
    }

    private void loadPhoto(final String stringURL) {
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
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
                if (mPhotoView != null) {
                    mPhotoView.setImageBitmap(bitmap);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
