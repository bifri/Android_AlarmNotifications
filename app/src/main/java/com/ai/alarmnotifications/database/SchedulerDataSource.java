package com.ai.alarmnotifications.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper for the database handler. Gives some CRUD (Create, Read, Update and
 * Delete) functionality to the scheduler database. Implemented as
 * a Singleton to allow for thread management,
 * call SchedulerDataSource.getInstance(Context) to get access to the instance
 * before using the database. THIS IS NOT A CLONABLE CLASS
 *
 * Contains SQLite table which stores PendingIntent IDs to be able to cancel
 * scheduled alarms.
 */

public class SchedulerDataSource {
    @SuppressWarnings("unused")
    private static final String TAG = "SchedulerDataSource";

    private static SchedulerDataSource instance;
    private SQLiteDatabase db;
    private SchedulerDBHelper handler;

    @SuppressWarnings("unused")
    private SchedulerDataSource() {

    }

    private SchedulerDataSource(Context context) {
        handler = new SchedulerDBHelper(context);
    }

    /**
     * Call this to get access to the instance of SchedulerDataSource Singleton.
     * @param context context
     * @return instance of SchedulerDataSource
     */
    public static synchronized SchedulerDataSource getInstance(Context context) {
        if (instance == null)
            instance = new SchedulerDataSource(context.getApplicationContext());
        return instance;
    }

    private void open() throws SQLException {
        db = handler.getWritableDatabase();
    }

    private void close() {
        handler.close();
    }

    /**
     * Query all notification IDs.
     * @return array of notification IDs
     */
    public Iterable<Integer> getNotificationIDs() {
        List<Integer> notifIds = new LinkedList<>();
        String selectQuery = "SELECT " + SchedulerDBHelper._ID +
                " FROM " + SchedulerDBHelper.TABLE_NOTIFICATIONS;
        open();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                notifIds.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return notifIds;
    }

    /**
     * Query a notification IDs corresponding to this notificationTypeID.
     * @param notifTypeID notificationTypeID
     * @return array of notification IDs
     */
    public Iterable<Integer> getNotificationIDs(int notifTypeID) {
        List<Integer> notifIds = new LinkedList<>();
        String selectQuery = "SELECT " + SchedulerDBHelper._ID +
                " FROM " + SchedulerDBHelper.TABLE_NOTIFICATIONS +
                " WHERE " + SchedulerDBHelper.NOTIFICATION_TYPE_ID +
                " = " + notifTypeID;
        open();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                notifIds.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return notifIds;
    }

    /**
     * Adds notifications of this notificationType.
     * @param notifTypeID notificationTypeID
     * @param notifQty number of notifications of this notificationType
     */
    public void addNotificationIDs(int notifTypeID, int notifQty) {
        ContentValues values = new ContentValues();
        open();
        // TABLE_NOTIFICATIONS update
        for (int i = 0; i < notifQty; i++) {
            values.put(SchedulerDBHelper.NOTIFICATION_TYPE_ID, notifTypeID);
            db.insert(SchedulerDBHelper.TABLE_NOTIFICATIONS, null, values);
        }
        close();
    }

    /**
     * Deletes all notifications from the database.
     * @return the number of notifications deleted
     */
    @SuppressWarnings("unused")
    public int deleteNotificationsIDs() {
        open();
        // TABLE_NOTIFICATIONS update
        int i = db.delete(SchedulerDBHelper.TABLE_NOTIFICATIONS, null, null);
        close();
        return i;
    }

    /**
     * Deletes all notifications of this notificationTypeID from the database.
     * @param notifTypeID notificationType ID
     * @return the number of notifications deleted
     */
    @SuppressWarnings("unused")
    public int deleteNotificationIDs(int notifTypeID) {
        open();
        // TABLE_NOTIFICATIONS update
        int i = db.delete(SchedulerDBHelper.TABLE_NOTIFICATIONS,
                SchedulerDBHelper.NOTIFICATION_TYPE_ID + " = " + notifTypeID,
                null);
        close();
        return i;
    }

    @Override
    @SuppressWarnings({"super", "CloneDoesntCallSuperClone"})
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
}