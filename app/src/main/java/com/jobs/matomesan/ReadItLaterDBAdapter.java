package com.jobs.matomesan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReadItLaterDBAdapter {
    private SQLiteDatabase db;
    private DBHelper helper;
    public static final String DATABASE_TABLE = "ReadItLater";

    public ReadItLaterDBAdapter(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public Cursor getAllList() {
        String orderStr = "_id desc";
        return db.query(DBHelper.TABLE_READITLATER, null, null, null, null, null, orderStr);
    }

    public void deleteRecode(int id) {
        db.delete(DBHelper.TABLE_READITLATER, "_id=?", new String[]{Integer.toString(id)});
    }

    public void deleteAllRecode() {
        db.delete(DBHelper.TABLE_READITLATER, null, null);
    }

    public void insert(ListItem item) {
        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("url", item.getLink());
        values.put("site", item.getSiteName());
        values.put("date", item.getDate());
        db.insertOrThrow(DBHelper.TABLE_READITLATER, null, values);
    }
}
