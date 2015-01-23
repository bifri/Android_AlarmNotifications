package com.ai.alarmnotifications.notification_type;

import android.app.Activity;

import com.ai.alarmnotifications.activity.Activity1;
import com.ai.alarmnotifications.activity.Activity2;
import com.ai.alarmnotifications.activity.Activity3;

/**
 * Contains all available notification types.
 */
public enum NotificationType implements AlarmSchedulable {
    LOW_IMPORTANCE(0, "Low importance", Activity1.class),
    NORMAL_IMPORTANCE(1, "Normal importance", Activity2.class),
    HIGH_IMPORTANCE(2, "High importance", Activity3.class);

    private final int id; // must be unique for every member
    private final String notifText;
    private final java.lang.Class<? extends Activity> activityClass;

    NotificationType(int id, String notifText,
                     java.lang.Class<? extends Activity> activityClass) {
        this.id = id;
        this.notifText = notifText;
        this.activityClass = activityClass;
    }

    @Override
    public int getUniqueId() {
        return id;
    }

    @Override
    public String getNotifText() {
        return notifText;
    }

    @Override
    public java.lang.Class<? extends Activity> getActivityClass() {
        return activityClass;
    }
}