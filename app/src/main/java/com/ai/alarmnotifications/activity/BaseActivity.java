package com.ai.alarmnotifications.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.ai.alarmnotifications.service.AppInBackgroundReceiver;
import com.ai.alarmnotifications.service.NotificationService;
import com.ai.alarmnotifications.service.WakefulIntentService;


public abstract class BaseActivity extends Activity {

    private static final String TAG = "BaseActivity";

    private static final long BACKGROUND_DELAY = 60000L;

    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Entered onCreate");
        super.onCreate(savedInstanceState);
        this.alarmManager = (AlarmManager) this.getSystemService
                (android.content.Context.ALARM_SERVICE);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "Entered onResume");
        super.onResume();
        // App is visible

        // check if alarm which scheduled to watch if app is in background
        // for 1 minute (== BACKGROUND_DELAY) has fired
        if (hasBackgroundAlarmFired()) {
            // Cancels all alarm notifications
            clearAllNotifications();
        }
        else {
            // Cancels alarm which scheduled to watch if app is in background
            // for 1 minute (== BACKGROUND_DELAY)
            cancelIsBackgroundAlarm();
        }
    }

    private boolean hasBackgroundAlarmFired() {
        Intent backgroundIntent = new Intent(this, AppInBackgroundReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, backgroundIntent,
                PendingIntent.FLAG_NO_CREATE);
        return pendingIntent == null;
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "Entered onPause");
        super.onPause();
        // App is going to background
        // Start alarm which after 1 minute launches NotificationService
        // to schedule AlarmNotifications
        // if this initial alarm is not cancelled before in onResume
        // when app becomes visible
        startIsBackgroundAlarm();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "Entered onStop");
        super.onStop();
    }

    private void clearAllNotifications() {
        // Acquire a partial WakeLock
        WakefulIntentService.acquireStaticLock(this);
        Intent service = new Intent(this, NotificationService.class);
        service.setAction(NotificationService.CLEAR);
        this.startService(service);
    }

    private void startIsBackgroundAlarm() {
        Intent intent = new Intent(this, AppInBackgroundReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + BACKGROUND_DELAY, pendingIntent);
    }

    private void cancelIsBackgroundAlarm() {
        Intent intent = new Intent(this, AppInBackgroundReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}
