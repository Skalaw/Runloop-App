package com.skala.runloop_app.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.skala.runloop_app.MemberAdapter;
import com.skala.runloop_app.MemberModel;
import com.skala.runloop_app.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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
        downloadMemberList();
    }

    private void downloadMemberList() {
        new AsyncTask<Void, Void, ArrayList<MemberModel>>() {
            @Override
            protected ArrayList<MemberModel> doInBackground(Void... params) {
                ArrayList<MemberModel> memberList = new ArrayList<>();
                String stringUrl = "http://runloop.pl/";

                try {
                    Document doc = Jsoup.connect(stringUrl).get();
                    Elements staffListContainer = doc.getElementsByClass("staff-list-container");
                    Elements members = staffListContainer.get(0).getElementsByClass("figure");

                    int sizeMember = members.size();
                    for (int i = 0; i < sizeMember; i++) {
                        Element member = members.get(i);
                        String imageUrl = member.getElementsByClass("figure-image").get(0).select("img").get(0).absUrl("src");
                        String description = member.getElementsByClass("figure-caption-description").get(0).text();

                        Element title = member.getElementsByClass("figure-caption-title").get(0);
                        String fullName = title.select("strong").text();
                        String position = title.select("span").text();

                        /*Log.i("MemberList", i + " - " + imageUrl);
                        Log.i("MemberList", i + " - " + fullName);
                        Log.i("MemberList", i + " - " + position);
                        Log.i("MemberList", i + " - " + description);*/

                        MemberModel memberModel = new MemberModel(imageUrl, fullName, position, description);
                        memberList.add(memberModel);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return memberList;
            }

            @Override
            protected void onPostExecute(ArrayList<MemberModel> memberList) {
                MemberAdapter memberAdapter = new MemberAdapter(getActivity(), memberList);
                mListView.setAdapter(memberAdapter);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
