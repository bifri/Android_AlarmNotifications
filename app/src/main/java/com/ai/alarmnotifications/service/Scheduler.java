package com.ai.alarmnotifications.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.ai.alarmnotifications.database.SchedulerDataSource;
import com.ai.alarmnotifications.notification_type.NotifData;
import com.ai.alarmnotifications.notification_type.NotificationType;

/**
 * Class for scheduling & clearing notification alarms.
 * Implemented as Singleton.
 */
public class Scheduler implements AlarmNotificator<NotificationType> {

    @SuppressWarnings("unused")
    private static final String TAG = "Scheduler";

    // extra which is forwarded to AlarmNotificationReceiver.class
    // to identify notification type
    protected static final String TYPE_NAME = "notificationTypeName";

    private static Scheduler instance;

    private AlarmManager alarmManager;
    private Context context;
    private SchedulerDataSource dataSource;

    @SuppressWarnings("unused")
    private Scheduler() {}

    private Scheduler(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService
                (android.content.Context.ALARM_SERVICE);
        this.dataSource = SchedulerDataSource.getInstance(context);
    }

    /**
     * Call this to get access to the instance of Scheduler Singleton.
     * @param context context
     * @return instance of Scheduler
     */
    public static synchronized Scheduler getInstance(Context context) {
        if (instance == null)
            instance = new Scheduler(context.getApplicationContext());
        return instance;
    }

    @Override
    public void clear() {
        cancelAllAlarms();
        // delete all notifications from storage
        dataSource.deleteNotificationsIDs();
    }

    @Override
    public void schedule(NotificationType notificationType) {
        long[] delays;
        if ((notificationType != null) &&
                ((delays = NotifData.getInstance().getDelays(notificationType)) != null)) {
            int id = notificationType.getUniqueId();
            // cancel alarms BEFORE delete from db
            cancelAlarms(id);
            // delete previous notifications from storage
            dataSource.deleteNotificationIDs(id);
            dataSource.addNotificationIDs(id, delays.length);
            setAlarms(notificationType, delays);
        }
    }

/*  // Extra option could be added to reschedule alarms after device restart
    // taking into account time passed from initial alarm start
    @Override
    public void reschedule() {
        cancelAllAlarms();
        NotificationType[] values = NotificationType.values();
        if (values != null) {
            for (NotificationType type : values) {
                if (isEnabled(type)) {
                    Log.i(TAG, "Entered reschedule");
                    setAlarms(type, getDelays(type));
                }
            }
        }
    }*/

    private void cancelAlarms(int typeId) {
        Iterable<Integer> ids = dataSource.getNotificationIDs(typeId);
        cancelPendingIntents(ids);
    }

    private void cancelAllAlarms() {
        Iterable<Integer> ids = dataSource.getNotificationIDs();
        cancelPendingIntents(ids);
    }

    private void cancelPendingIntents(Iterable<Integer> ids) {
        if (ids.iterator().hasNext()) {
            for (int id : ids) {
                // get pendingIntent of this alarm,
                // it's unique id == requestCode of pendingIntent
                Intent intent = new Intent(context, AlarmNotificationReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent,
                        PendingIntent.FLAG_NO_CREATE);
                if (pendingIntent != null) {
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                }
            }
        }
    }

    private void setAlarms(NotificationType type, long[] delays) {
        if (type != null && delays != null) {
            int typeId = type.getUniqueId();
            Iterable<Integer> ids = dataSource.getNotificationIDs(typeId);
            PendingIntent pendingIntent;
            int ind = 0;
            for (int id : ids) {
                if (delays[ind] > 0) {
                    // Create an Intent to broadcast to the AlarmNotificationReceiver
                    // All alarms of this notificationType are set at once using
                    // different request codes of pendingIntent
                    // Another option is to set next alarm when previous has fired
                    Intent intent = new Intent(context, AlarmNotificationReceiver.class);
                    intent.putExtra(TYPE_NAME, type.name());
                    pendingIntent = PendingIntent.getBroadcast(context, id, intent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + delays[ind++], pendingIntent);
                }
            }
        }
    }
}
