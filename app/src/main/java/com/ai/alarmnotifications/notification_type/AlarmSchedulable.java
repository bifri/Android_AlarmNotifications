package com.ai.alarmnotifications.notification_type;

import android.app.Activity;

public interface AlarmSchedulable {

    /**
     * Returns unique ID.
     * @return unique ID
     */
    @SuppressWarnings("unused")
    int getUniqueId();

    /**
     * Returns unique ID.
     * @return unique ID
     */
    @SuppressWarnings("unused")
    String getNotifText();

    /**
     * Returns Activity class.
     * @return Activity class
     */
    @SuppressWarnings("unused")
    java.lang.Class<? extends Activity> getActivityClass();

/*    class Utils {
        public static <T extends Enum<T> & AlarmSchedulable> int[] getIds(
                java.lang.Class<T> NotificationType) {
            T[] values = NotificationType.getEnumConstants();
            int length = values.length;
            int[] result = new int[length];
            for (int i = 0; i < length; i++) {
                result[i] = values[i].getUniqueId();
            }
            return result;
        }
    }
*/
}
