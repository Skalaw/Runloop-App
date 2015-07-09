package com.skala.runloop_app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.skala.runloop_app.MemberAdapter;
import com.skala.runloop_app.MemberModel;
import com.skala.runloop_app.R;

import java.util.ArrayList;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        MemberModel memberModel = new MemberModel("Test1", "Test11", "Test111", "Test1111");
        MemberModel memberModel2 = new MemberModel("Test2", "Test22", "Test222", "Test2222");

        ArrayList<MemberModel> memberList = new ArrayList<>();
        memberList.add(memberModel);
        memberList.add(memberModel2);

        MemberAdapter memberAdapter = new MemberAdapter(getActivity(), memberList);
        mListView.setAdapter(memberAdapter);
    }
}
