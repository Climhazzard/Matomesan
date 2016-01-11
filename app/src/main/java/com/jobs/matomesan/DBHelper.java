package com.jobs.matomesan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "datastore.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_HISTORY = "History";
    public static final String TABLE_MYLIST = "MyList";
    public static final String TABLE_MYLISTCONTENTS = "MyListContents";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE History (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT NOT NULL, " +
                        "url TEXT NOT NULL, " +
                        "site TEXT NOT NULL, " +
                        "date TEXT NOT NULL)"

        );
        db.execSQL(
                "CREATE TABLE MyList (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "flag INTEGER NOT NULL)"
        );
        db.execSQL(
                "CREATE TABLE MyListContents (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "mylist_id INTEGER NOT NULL, " +
                        "site TEXT NOT NULL, " +
                        "url TEXT NOT NULL, " +
                        "flag INTEGER NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HistoryDBAdapter.DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MyListDBAdapter.DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MyListContentsDBAdapter.DATABASE_TABLE);
        onCreate(db);
    }
}
