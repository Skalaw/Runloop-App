package com.skala.runloop_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.skala.runloop_app.fragments.MemberListFragment;
import com.skala.runloop_app.models.MemberModel;

/**
 * @author Skala
 */
public class MainActivity extends FragmentActivity implements MemberListFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onItemSelected(MemberModel member) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MEMBER_KEY, member);
        startActivity(intent);
    }
}
