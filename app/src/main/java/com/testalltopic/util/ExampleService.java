package com.testalltopic.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.testalltopic.R;
import com.testalltopic.ui.fragment.ServiceFragment;

import static android.app.Application.getProcessName;
import static com.testalltopic.util.App.CHANNEL_ID;

public class ExampleService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputData");

        Intent notificationIntent = new Intent(this, ServiceFragment.class);
        PendingIntent pendingIntent = null;
        try {
             pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        }catch (Exception e){
            e.printStackTrace();
        }

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
//        stopSelf();
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
