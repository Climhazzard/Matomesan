package com.jobs.matomesan;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "datastore.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_HISTORY = "History";
    public static final String TABLE_MYLIST = "MyList";
    public static final String TABLE_MYLISTCONTENTS = "MyListContents";
    public static final String TABLE_READITLATER = "ReadItLater";
    public static final String TABLE_BOOKMARK = "BookMark";
    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
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
                        "flag INTEGER NOT NULL, " +
                        "FOREIGN KEY(mylist_id) REFERENCES MyList(_id) ON DELETE CASCADE)"
        );
        db.execSQL(
                "CREATE TABLE ReadItLater (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT NOT NULL, " +
                        "url TEXT NOT NULL, " +
                        "site TEXT NOT NULL, " +
                        "date TEXT NOT NULL)"
        );
        db.execSQL(
                "CREATE TABLE BookMark (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT NOT NULL, " +
                        "url TEXT NOT NULL, " +
                        "site TEXT NOT NULL, " +
                        "date TEXT NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL("DROP TABLE IF EXISTS " + ReadItLaterDBAdapter.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + BookMarkDBAdapter.DATABASE_TABLE);
            db.execSQL(
                    "CREATE TABLE ReadItLater (" +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "title TEXT NOT NULL, " +
                            "url TEXT NOT NULL, " +
                            "site TEXT NOT NULL, " +
                            "date TEXT NOT NULL)"
            );
            db.execSQL(
                    "CREATE TABLE BookMark (" +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "title TEXT NOT NULL, " +
                            "url TEXT NOT NULL, " +
                            "site TEXT NOT NULL, " +
                            "date TEXT NOT NULL)"
            );
        }
    }
}
