package com.android.fpuna.client;

import android.app.PendingIntent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

/**
 * Created by agimenez on 16/08/2015.
 */
public class MyActivityRecognition {

    public static ActivityRecognitionImpl ActivityRecognitionApi;

    private MyActivityRecognition() {

    }

    static {
        ActivityRecognitionApi = new ActivityRecognitionImpl();
    }
}
