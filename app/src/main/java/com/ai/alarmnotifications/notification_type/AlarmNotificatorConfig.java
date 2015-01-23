package com.ai.alarmnotifications.notification_type;

public interface AlarmNotificatorConfig<T extends Enum<T>> {

    /**
     * @return false если нотификация данного типа отключена
     */
    boolean isEnabled(T notificationType);

    /**
     * @return массив задержек показа нотификации относительно текущего момента
     */
    @SuppressWarnings("unused")
    long[] getDelays(T notificationType);

}
