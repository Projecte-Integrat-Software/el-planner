package com.example.our_planner;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.our_planner.ui.user.LoginActivity;

public abstract class Notifier {

    public static void sendNotification(Context c, String s) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = c.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(c, LoginActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(c);
        stackBuilder.addNextIntentWithParentStack(notificationIntent);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Resources r = LocaleLanguage.getLocale(c).getResources();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(c, "My Notification").
                setContentTitle(r.getString(R.string.new_invitation)).setContentText(s).setSmallIcon(R.drawable.ic_launcher).setLargeIcon(BitmapFactory. decodeResource (c.getResources() , R.drawable.ic_launcher)).
                setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).setAutoCancel(true).setContentIntent(intent);

        NotificationManagerCompat manager = NotificationManagerCompat.from(c);
        manager.notify(0, builder.build());
    }
}
