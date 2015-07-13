package com.skala.runloop_app.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
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
    private ProgressDialog mProgressDialog;

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
            showProgressDialog(R.string.message_loading_member_download);
        } else {
            if (!Utility.isMyServiceRunning(getActivity(), DownloadMemberService.class)) { // if service downloading members - don't load member (wait for Broadcast)
                loadMembersFromDataBase();
            } else {
                showProgressDialog(R.string.message_loading_member_download); // continue to show dialog
            }
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(DownloadMemberService.DOWNLOAD_MEMBER)) {
                releaseProgressDialog(); // release message download
                loadMembersFromDataBase();

                boolean successfully = intent.getBooleanExtra(DownloadMemberService.KEY_RESULT, false);
                if (!successfully) {
                    Toast.makeText(getActivity(), R.string.message_member_download_error, Toast.LENGTH_SHORT).show();
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
        new AsyncTask<Void, Void, ArrayList<MemberModel>>() {

            @Override
            protected void onPreExecute() {
                showProgressDialog(R.string.message_loading_member_load);
            }

            @Override
            protected ArrayList<MemberModel> doInBackground(Void... params) {
                MembersSQLHelper membersSQLHelper = new MembersSQLHelper(getActivity());
                return membersSQLHelper.getAllMembers();
            }

            @Override
            protected void onPostExecute(ArrayList<MemberModel> memberList) {
                Context context = getActivity();
                if (memberList != null && context != null) {
                    mMemberAdapter = new MemberAdapter(context, memberList);
                    mListView.setAdapter(mMemberAdapter);
                }

                releaseProgressDialog();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onStop() {
        super.onStop();

        releaseProgressDialog(); // we need to catch WindowLeaked
    }

    private void showProgressDialog(int stringID) {
        Context context = getActivity();
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(context.getString(stringID));
        mProgressDialog.show();
    }

    private void releaseProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
