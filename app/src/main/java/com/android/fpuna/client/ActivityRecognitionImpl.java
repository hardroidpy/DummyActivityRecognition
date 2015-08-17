package com.android.fpuna.client;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.android.fpuna.dummyactivityrecognition.Constants;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognitionApi;

public class ActivityRecognitionImpl {

    public PendingResult<Status> requestActivityUpdates(Context context, long l, PendingIntent pendingIntent) {
        Intent intent = new Intent(Constants.REGISTER_ACTION);
        intent.putExtra(Constants.ACTION_SERVICE_EXTRA, pendingIntent);
        intent.putExtra(Constants.ACTION_TIME_EXTRA, l);
        context.sendBroadcast(intent);
        return null;

    }

    public PendingResult<Status> removeActivityUpdates(Context context, PendingIntent pendingIntent) {
        Intent intent = new Intent(Constants.UNREGISTER_ACTION);
        intent.putExtra(Constants.ACTION_SERVICE_EXTRA, pendingIntent);
        context.sendBroadcast(intent);
        return null;
    }
}