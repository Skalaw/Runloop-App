package com.skala.runloop_app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skala.runloop_app.DetailActivity;
import com.skala.runloop_app.MemberModel;
import com.skala.runloop_app.R;

/**
 * @author Skala
 */
public class DetailFragment extends Fragment {
    private MemberModel mMemberModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMemberModel = arguments.getParcelable(DetailActivity.MEMBER_KEY);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        return rootView;
    }
}
