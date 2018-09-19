package com.developer.abdulah.backgroundservice.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.developer.abdulah.backgroundservice.MainActivity;
import com.developer.abdulah.backgroundservice.db.Database;
import com.developer.abdulah.backgroundservice.db.Task;

/**
 * Created by dev on 9/17/18.
 */

public class BackgroundService extends Service implements Runnable {

    Thread th;
    boolean isStopped = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (!th.isAlive()) {
//            th.start();
//        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        ContextCompat.startForegroundService(getApplicationContext(), new Intent(getApplicationContext(), BackgroundService.class));
//        startForeground(1234, notification());
//        th = new Thread(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStopped = true;
    }

    Notification notification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "my_service_channelid";

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setContentTitle("Processing tasks")
                .setContentText("tasks...")
                .setContentIntent(pendingIntent)
                .build();
    }

    Notification notificationFor(Task t) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "my_service_channelid";

        NotificationCompat.Builder b = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true)
                .setContentTitle(t.getName())
                .setContentText(t.getStatus());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        return b.setContentIntent(pendingIntent).build();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager notificationManager){
        String channelId = "my_service_channelid";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        // omitted the LED color
        channel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    @Override
    public void run() {
        Database db = new Database(this);
        int i = 0;
        while (true) {
            try {
                Task t = db.createTask("Task " + i++, "started");
                startForeground(12345, notificationFor(t));
                Thread.sleep(1 * 1000);

                t.setStatus("processing");
                db.save(t);
                Thread.sleep(15 * 1000);

                t.setStatus("almost finished");
                db.save(t);
                Thread.sleep(10 * 1000);

                t.setStatus("finished");
                db.save(t);
//                stopForeground(true);
                Thread.sleep(10 * 1000);

                if (isStopped) {
                    return;
                }

            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

