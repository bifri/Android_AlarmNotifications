package com.ai.alarmnotifications.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.ai.alarmnotifications.R;
import com.ai.alarmnotifications.notification_type.NotificationType;

/**
 * Alarm receiver which launches Notifications
 */
public class AlarmNotificationReceiver extends BroadcastReceiver {

    @SuppressWarnings("unused")
    private static final String TAG = "AlarmNotificationReceiver";

    // Notification Text Elements
    private final CharSequence contentTitle = "Alarm Notifications";

    // Notification Sound and Vibration on Arrival
//	private final Uri soundURI = Uri
//			.parse("android.resource://course.examples.Alarms.AlarmCreate/"
//					+ R.raw.alarm_rooster);

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationType type = NotificationType.valueOf(intent.getStringExtra
                (Scheduler.TYPE_NAME));
        // The Intent to be used when the user clicks on the Notification View
        Intent notificationIntent = new Intent(context, type.getActivityClass());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // The PendingIntent that wraps the underlying Intent
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Build the Notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
//      		    .setSound(soundURI)
//		        	.setVibrate(mVibratePattern);
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setTicker(type.getNotifText())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setAutoCancel(true)
                    .setContentTitle(contentTitle)
                    .setContentText(type.getNotifText())
                    .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(type.getUniqueId(),
                notificationBuilder.build());
    }
}
