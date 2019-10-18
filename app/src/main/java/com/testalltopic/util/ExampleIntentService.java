package com.testalltopic.util;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.testalltopic.R;

import static com.testalltopic.util.App.CHANNEL_ID;

public class ExampleIntentService extends IntentService {
    public static final String TAG = "ExampleIntentService";
    private PowerManager.WakeLock wakeLock;
    public ExampleIntentService() {
        super("ExampleIntentService");
        // its work same as START_REDELIVER_INTENT
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        PowerManager powerManager =  (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "ExampleApp:wakelock");
        wakeLock.acquire();
        Log.d(TAG, "wakeLock acquire");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            Notification notification = new Notification.Builder(this,CHANNEL_ID)
                    .setContentTitle("Example Intent Service")
                    .setContentText("Running ...")
                    .setSmallIcon(R.drawable.ic_android)
                    .build();

            startForeground(1,notification);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(TAG, "onHandleIntent");

        String input = intent.getStringExtra("inputData");

        for (int i = 0; i <10 ; i++) {
            Log.d(TAG, input +" "+ i);
            SystemClock.sleep(1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        wakeLock.release();
        Log.d(TAG, "wakelock Release");
    }
}
