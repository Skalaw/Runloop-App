package com.skala.runloop_app.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.skala.runloop_app.MemberAdapter;
import com.skala.runloop_app.R;
import com.skala.runloop_app.models.MemberModel;
import com.skala.runloop_app.services.DownloadMemberService;
import com.skala.runloop_app.sql.MembersSQLHelper;
import com.skala.runloop_app.utils.Utility;

import java.util.ArrayList;

/**
 * @author Skala
 */
public class MemberListFragment extends Fragment {
    private ListView mListView;
    private MemberAdapter mMemberAdapter;

    public interface Callback {
        void onItemSelected(MemberModel member);
    }

    public MemberListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.fragment_member_list, container, false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemberModel memberModel = (MemberModel) mMemberAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(memberModel);
            }
        });

        return mListView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = new Intent(getActivity(), DownloadMemberService.class);
            getActivity().startService(intent);
        } else {
            if (!Utility.isMyServiceRunning(getActivity(), DownloadMemberService.class)) { // if service downloading members - don't load member (wait for Broadcast)
                loadMembersFromDataBase();
            }
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(DownloadMemberService.DOWNLOAD_MEMBER)) {
                loadMembersFromDataBase();

                boolean successfully = intent.getBooleanExtra(DownloadMemberService.KEY_RESULT, false);
                if (!successfully) {
                    Toast.makeText(getActivity(), "You can't download member list. Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mReceiver, new IntentFilter(DownloadMemberService.DOWNLOAD_MEMBER));
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReceiver);
        super.onPause();
    }

    private void loadMembersFromDataBase() {
        MembersSQLHelper membersSQLHelper = new MembersSQLHelper(getActivity());
        ArrayList<MemberModel> memberList = membersSQLHelper.getAllMembers();

        if (memberList != null) {
            mMemberAdapter = new MemberAdapter(getActivity(), memberList);
            mListView.setAdapter(mMemberAdapter);
        }
    }
}
