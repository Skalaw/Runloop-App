package com.skala.runloop_app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.skala.runloop_app.utils.NetworkUtils;

/**
 * @author Skala
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    public static final String INTERNET_IS_CONNECTED = "com.skala.runloop_app.receivers.NetworkChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtils.isInternetConnected(context)) {
            Intent intentBroadcast = new Intent(INTERNET_IS_CONNECTED);
            context.sendBroadcast(intentBroadcast);
        }
    }
}
