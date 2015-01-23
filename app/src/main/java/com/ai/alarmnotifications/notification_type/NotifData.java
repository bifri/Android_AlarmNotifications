package com.ai.alarmnotifications.notification_type;


import java.util.EnumMap;

/**
 * Class with hardcoded values just for testing of AlarmNotificator.
 */
public class NotifData implements AlarmNotificatorConfig<NotificationType> {

    @SuppressWarnings("unused")
    private static final String TAG = "NotifData";

    private static NotifData instance;

    private EnumMap<NotificationType, Boolean> isEnabledMap;
    private EnumMap<NotificationType, long[]> delaysMap;

    @SuppressWarnings("unused")
    private NotifData() {
        setHardcodedValues();
    }

    public static synchronized NotifData getInstance() {
        if (instance == null)
            instance = new NotifData();
        return instance;
    }

    private void setHardcodedValues() {
        isEnabledMap = new EnumMap<>(NotificationType.class);
        delaysMap = new EnumMap<>(NotificationType.class);
        boolean defaultStatus = true;
        long[][] delays = new long[3][6];
        delays[0] = new long[] {10000L, 40000L, 100000L, 160000L, 220000L, 300000L};
        delays[1] = new long[] {60000L, 70000L, 80000L, 90000L, 100000L, 110000L};
        delays[2] = new long[] {70000L, 130000L, 190000L, 250000L, 330000L, 600000L};

//        delays[0] = new long[] {10000L, 15000L, 30000L, 60000L, 90000L, 120000L};
//        delays[1] = new long[] {60000L, 70000L, 80000L, 90000L, 100000L, 110000L};
//        delays[2] = new long[] {20000L, 25000L, 40000L, 50000L, 70000L, 100000L};
        NotificationType[] values = NotificationType.values();
        int ind = 0;
        for(NotificationType type: values) {
            isEnabledMap.put(type, defaultStatus);
            defaultStatus = !defaultStatus;
            delaysMap.put(type, delays[ind % 3]);
            ind++;
        }
    }

    @Override
    public boolean isEnabled(NotificationType type) {
        return (type != null) && isEnabledMap.get(type);
    }

    @Override
    public long[] getDelays(NotificationType type) {
        if (type != null) {
            return delaysMap.get(type);
        }
        return null;
    }
}