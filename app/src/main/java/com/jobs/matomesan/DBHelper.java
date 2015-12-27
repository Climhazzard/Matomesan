package com.jobs.matomesan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "datastore.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "History";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + "(" +
                        "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Title TEXT NOT NULL, " +
                        "URL TEXT NOT NULL, " +
                        "Site TEXT NOT NULL, " +
                        "Date TEXT NOT NULL)"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
