package com.skala.runloop_app.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.skala.runloop_app.models.MemberModel;
import com.skala.runloop_app.sql.MembersSQLHelper;
import com.skala.runloop_app.utils.MemCache;
import com.skala.runloop_app.utils.NetworkUtils;

import java.util.ArrayList;

/**
 * @author Skala
 */
public class DownloadMemberService extends IntentService {
    private static final String TAG = MemCache.class.getSimpleName();

    private static final String URL_RUNLOOP_SITE = "http://runloop.pl/";

    public static final String DOWNLOAD_MEMBER = "com.skala.runloop_app.services.DownloadMemberService";
    public static final String KEY_RESULT = "result";

    public DownloadMemberService() {
        super("DownloadMemberService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<MemberModel> memberList = NetworkUtils.downloadMemberList(URL_RUNLOOP_SITE);
        if (memberList != null) {
            MembersSQLHelper membersSQLHelper = new MembersSQLHelper(getApplicationContext());
            membersSQLHelper.addMembers(memberList);
            Log.d(TAG, "Data download successfully");
        } else {
            Log.d(TAG, "Data download failure");
        }

        Intent intentBroadcast = new Intent(DOWNLOAD_MEMBER);
        intentBroadcast.putExtra(KEY_RESULT, memberList != null);
        sendBroadcast(intentBroadcast);
    }
}
