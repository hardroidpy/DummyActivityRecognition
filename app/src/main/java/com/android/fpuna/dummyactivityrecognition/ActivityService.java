package com.android.fpuna.dummyactivityrecognition;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.ActivityRecognitionResultCreator;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.DetectedActivityCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityService extends Service {
    public static final String TAG = ActivityService.class.getSimpleName();
    private boolean runFlag = false;
    protected Timer timer;
    protected HashMap<Integer, ActivityClientUpdater> clients = new HashMap<>();
    private ActivityClientReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.runFlag = false;
        this.timer = new Timer();
        this.receiver = new ActivityClientReceiver();
        registerReceiver(receiver, new IntentFilter(Constants.REGISTER_ACTION));
        registerReceiver(receiver, new IntentFilter(Constants.UNREGISTER_ACTION));

        Log.d(TAG, "Create Activity Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (!this.runFlag) {
            Log.v(TAG, "Activity Service Initiated");
            this.runFlag = true;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.runFlag = false;
        this.timer.cancel();
        this.timer.purge();
        this.timer = null;

        Log.d(TAG, "Destroy Activity Service");
    }

    private class ActivityClientUpdater extends TimerTask {
        private PendingIntent intentService;
        private Context context;
        private int[] activities =  new int[]{DetectedActivity.WALKING, DetectedActivity.UNKNOWN,
                DetectedActivity.RUNNING, DetectedActivity.IN_VEHICLE,
                DetectedActivity.ON_BICYCLE, DetectedActivity.STILL,
                DetectedActivity.ON_FOOT, DetectedActivity.TILTING};

        public ActivityClientUpdater(Context context, PendingIntent intentService) {
            Log.d(TAG, "Recibiendo cliente de actividad");
            this.intentService = intentService;
            this.context = context;
        }

        private ActivityRecognitionResult getResults() {
            ArrayList<DetectedActivity> detected = new ArrayList<>();
            for (int act : activities) {
                DetectedActivity de = new DetectedActivity(act,
                        (int) (Math.random() * 100 + 1));
                detected.add(de);
            }
            return new ActivityRecognitionResult(1, detected, 0, 0);
        }

        @Override
        public void run() {
            ActivityService activityService = ActivityService.this;
            Intent intent = new Intent();
            intent.putExtra("com.google.android.location.internal.EXTRA_ACTIVITY_RESULT",
                    getResults());
            try {
                this.intentService.send(this.context, 0, intent);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

        }
    }

    private class ActivityClientReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.REGISTER_ACTION)) {
                PendingIntent pe = (PendingIntent) intent.getExtras()
                        .get(Constants.ACTION_SERVICE_EXTRA);
                long millis = intent.getLongExtra(Constants.ACTION_TIME_EXTRA, 10000);
                Integer key = pe.hashCode();
                clients.put(key, new ActivityClientUpdater(context, pe));
                timer.schedule(clients.get(key), millis, millis);
            }
            else if (intent.getAction().equals(Constants.UNREGISTER_ACTION)) {
                PendingIntent pe = (PendingIntent) intent.getExtras()
                        .get(Constants.ACTION_SERVICE_EXTRA);
                Integer key = pe.hashCode();
                clients.get(key).cancel();
                clients.remove(key);
            }
            else {
                return;
            }
        }
    }
}
