package com.ai.alarmnotifications.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * BroadCastReceiver for android.intent.action.BOOT_COMPLETED
 * Passes all responsibility to NotificationService.
 * All alarm notifications are rescheduled on device restart.
 */
public class OnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            //acquire a partial WakeLock
            WakefulIntentService.acquireStaticLock(context);
            Intent service = new Intent(context, NotificationService.class);
            service.setAction(NotificationService.SCHEDULE);
            context.startService(service);
        }
    }
}
