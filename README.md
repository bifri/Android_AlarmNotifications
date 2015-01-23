# Android AlarmNotifications sample app

## About
Show notifications according to schedule

## Technical description
- show notifications (android.app.Notification) according to schedule with indicated intervals
- realization uses AlarmManager
- there is only 1 notification of certain type in notification area (no accumulation)
- notifications are put in schedule after 1 minute after app has gone in background
(app can be stopped anytime through TaskManager)
- after app is back from background schedule is cleared (calling "AlarmNotificator.clear()")
- there is different Activity for every notification type
- app is working starting from API LEVEL 10
- app is build using Gradle + obfuscation with ProGuard

## Example
If there is array of delays {1000,5000,10000} indicated for certain notification type X
then notifications should be shown after approximately 1, 5 and 10 seconds after
AlarmNotificator.schedule(T notificationType) was called if series of shows wasn't cancelled
by AlarmNotificator.clear()

##API
```
interface AlarmNotificator<T extends Enum<T>> {  
  /**
   * Clears notification schedule - cancels all previously scheduled notifications
   */
  void clear();

  /**
   * Puts notifications in schedule.
   * Cancels all notifications of this type scheduled earlier.
   */
  void schedule(T notificationType);
}
```

```
interface AlarmNotificatorConfig<T extends Enum<T>> {
  /**
   * @return false if notifications of this type are turned off 
   */

  boolean isEnabled(T notificationType);
  /**
   * @return array of notification show delays
   */
  long[] getDelays(T notificationType);
}
```