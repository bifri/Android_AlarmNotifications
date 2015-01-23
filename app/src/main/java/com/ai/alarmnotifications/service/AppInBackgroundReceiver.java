package com.ai.alarmnotifications.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AppInBackgroundReceiver extends BroadcastReceiver {

    @SuppressWarnings("unused")
    private static final String TAG = "AppInBackgroundReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // acquire a partial WakeLock
        WakefulIntentService.acquireStaticLock(context);

        // this is the Intent to deliver to our service
        Intent service = new Intent(context, NotificationService.class);
        service.setAction(NotificationService.SCHEDULE);

        // start NotificationService
        context.startService(service);

        // Cancel pendingIntent which used by alarmManager to trigger this
        // broadcastReceiver so next time app becomes visible it could be checked
        // if alarm has fired.
        // If it hasn't fired there is no need to cancel scheduled notification alarms
        // just because there is no any.
        Intent backgroundIntent = new Intent(context, AppInBackgroundReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                backgroundIntent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService
                    (android.content.Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}
