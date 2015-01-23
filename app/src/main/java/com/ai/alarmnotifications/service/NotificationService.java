package com.ai.alarmnotifications.service;

import android.content.Intent;

import com.ai.alarmnotifications.notification_type.AlarmNotificatorConfig;
import com.ai.alarmnotifications.notification_type.NotifData;
import com.ai.alarmnotifications.notification_type.NotificationType;

/**
  * Service which clears & schedules notification alarms using Scheduler object.
  */
public class NotificationService extends WakefulIntentService {

    @SuppressWarnings("unused")
    private static final String TAG = "NotificationService";

    public static final String SCHEDULE = "com.ai.alarmnotifications.SCHEDULE";
    public static final String CLEAR = "com.ai.alarmnotifications.CANCEL_ALARM";

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Scheduler scheduler = Scheduler.getInstance(this);
        switch (intent.getAction()) {
            case CLEAR:
                scheduler.clear();
                break;
            case SCHEDULE:
                AlarmNotificatorConfig<NotificationType> notifData = NotifData.getInstance();
                // schedules all enabled notification types
                for(NotificationType type: NotificationType.values()) {
                    if (notifData.isEnabled(type)) { scheduler.schedule(type); }
                }
                break;
            default:
                break;
        }

        // needed to release a partial WakeLock
        super.onHandleIntent(intent);
    }
}
