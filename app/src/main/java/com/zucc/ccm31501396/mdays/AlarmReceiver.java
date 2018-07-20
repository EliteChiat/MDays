package com.zucc.ccm31501396.mdays;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String clockId = intent.getStringExtra("Clock");
        Log.d("传来的clockId值", clockId);
        if(action == SingleSchedule.INTENT_ALARM_LOG){
            Log.d("naozhong", "test");
            NotificationManager mn = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            Notification notification = new NotificationCompat.Builder(context).build();
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
            builder.setContentTitle("您有日程开始了");
            builder.setContentText("快去完成你的日程吧！！！");
            builder.setWhen(System.currentTimeMillis());
            builder.setAutoCancel(true);

            mn.notify(Integer.parseInt(clockId),builder.build());

        }
    }
}
