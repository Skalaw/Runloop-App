package com.skala.runloop_app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.skala.runloop_app.fragments.DetailFragment;

/**
 * @author Skala
 */
public class DetailActivity extends FragmentActivity {
    public static final String MEMBER_KEY = "member";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            MemberModel memberModel = getIntent().getParcelableExtra(MEMBER_KEY);

            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivity.MEMBER_KEY, memberModel);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.member_detail_container, fragment)
                    .commit();
        }
    }
}
