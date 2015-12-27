package com.jobs.matomesan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
    private SQLiteDatabase db;
    private DBHelper helper;

    public DBAdapter(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public Cursor getAllList() {
        String orderStr = "Id desc";
        return db.query(DBHelper.TABLE_NAME, null, null, null, null, null, orderStr);
    }

    public void insert(listItem item) {
        ContentValues values = new ContentValues();
        values.put("Title", item.getTitle());
        values.put("URL", item.getLink());
        values.put("Site", item.getSiteName());
        values.put("Date", item.getDate());
        db.insertOrThrow(DBHelper.TABLE_NAME, null, values);
    }
}