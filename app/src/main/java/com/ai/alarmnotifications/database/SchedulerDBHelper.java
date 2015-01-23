package com.ai.alarmnotifications.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Creates SQLite table for storing PendingIntent IDs to be able to cancel
 * scheduled alarms.
 * DO NOT call this class directly get an instance of SchedulerDataSource instead
 */
public class SchedulerDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "scheduler_db";
    private static final int DB_VERSION = 1;

    // Tables
    static final String TABLE_NOTIFICATIONS = "notifs";

    // Column names
    static final String _ID = "_id";                    // INTEGER PRIMARY KEY in TABLE_NOTIFICATIONS
    static final String NOTIFICATION_TYPE_ID = "type";  // INTEGER

    public SchedulerDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private void createNotifsTable(SQLiteDatabase db) {
        String create_notifs_table = "CREATE TABLE " + TABLE_NOTIFICATIONS +
                " (" + _ID + " INTEGER PRIMARY KEY," +
                NOTIFICATION_TYPE_ID + " INTEGER NOT NULL)";
        db.execSQL(create_notifs_table);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createNotifsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // N/A
    }
}
