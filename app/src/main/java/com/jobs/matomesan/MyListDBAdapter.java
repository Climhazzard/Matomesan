package com.jobs.matomesan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyListDBAdapter {
    private SQLiteDatabase db;
    private DBHelper helper;
    public static final String DATABASE_TABLE = "MyList";

    public MyListDBAdapter(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void makeDefaultMyList() {
        ContentValues values = new ContentValues();
        values.put("name", "サイト一覧 (default)");
        values.put("created_at", "a");
        db.insertOrThrow(DBHelper.TABLE_MYLIST, null, values);
    }

    public Cursor getMyList() {
        return db.query(DBHelper.TABLE_MYLIST, new String[]{"id", "name"}, null, null, null, null, null);
    }
}