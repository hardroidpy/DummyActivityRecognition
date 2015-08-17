package com.android.fpuna.dummyactivityrecognition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    public static final String TAG = BootReceiver.class.getSimpleName();

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ActivityService.class));
        Log.d(TAG, "Boot Reciver");
    }
}
