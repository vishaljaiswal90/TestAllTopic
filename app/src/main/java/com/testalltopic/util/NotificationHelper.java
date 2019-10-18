package com.testalltopic.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.testalltopic.R;

import static com.testalltopic.util.App.CHANNEL_ID;

public class NotificationHelper {
    public static void displayNotification(Context context,String title,String body) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") Notification.Builder builder = new Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_android)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
            managerCompat.notify(1, builder.build());
        } else {
            Toast.makeText(context, "not working", Toast.LENGTH_SHORT).show();
        }

    }

}
