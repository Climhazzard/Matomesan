package com.jobs.matomesan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HistoryDBAdapter {
    private SQLiteDatabase db;
    private DBHelper helper;
    public static final String DATABASE_TABLE = "History";

    public HistoryDBAdapter(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public Cursor getAllList() {
        String orderStr = "id desc";
        return db.query(DBHelper.TABLE_HISTORY, null, null, null, null, null, orderStr);
    }

    public void deleteRecode(String key) {
        db.delete(DBHelper.TABLE_HISTORY, "url=?", new String[]{key});
    }

    public void deleteAllRecode() {
        db.delete(DBHelper.TABLE_HISTORY, null, null);
    }

    public void insert(ListItem item) {
        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("url", item.getLink());
        values.put("site", item.getSiteName());
        values.put("date", item.getDate());
        db.insertOrThrow(DBHelper.TABLE_HISTORY, null, values);
    }
}