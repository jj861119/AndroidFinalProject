package com.example.finalproject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager m_notificationMgr = null;
    private static final int NOTIFICATION_FLAG = 3;
    @Override
    public void onReceive(Context context, Intent intent) {
        m_notificationMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (intent.getAction().equals("android.alarm.demo.action")) {
            Log.e("alarm_receiver", "週期鬧鐘");
            Notify(context);
            testFirebase.alarm.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +5*1000, testFirebase.sender);
        }

    }

    // new 一個 method
    public void Notify(Context context) {
        //Log.i("notify","notifyyyyyyyyyyyyyyyyyyyy");
        Log.i("packageName",context.getPackageName());

        Notification.Builder builder = new Notification.Builder(context);
        Intent intent = new Intent(context, QuestionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("測試")
                .setContentTitle("Android期末專案")
                .setContentText("快來進行問答挑戰!!!!")
                .setContentIntent(pendingIntent);

        NotificationChannel channel;
        String channelId = "some_channel_id";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            channel = new NotificationChannel(channelId
                    ,"Notify Test"
                    ,NotificationManager.IMPORTANCE_HIGH);
            builder.setChannelId(channelId);
            m_notificationMgr.createNotificationChannel(channel);
        }else{
            builder.setDefaults(Notification.DEFAULT_ALL)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }


        Notification notification = builder.build();
        m_notificationMgr.notify(0, notification);

    }
}