package com.skala.runloop_app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.skala.runloop_app.R;

/**
 * @author Skala
 */
public class MemberListFragment extends Fragment {
    private ListView mListView;

    public MemberListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.fragment_member_list, container, false);

        return mListView;
    }
}
