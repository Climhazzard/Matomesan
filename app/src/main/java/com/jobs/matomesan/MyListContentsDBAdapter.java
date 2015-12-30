package com.jobs.matomesan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class MyListContentsDBAdapter {
    private SQLiteDatabase db;
    private DBHelper helper;
    public static final String DATABASE_TABLE = "MyListContents";

    public MyListContentsDBAdapter(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public Cursor getURLList() {
        return db.query(DBHelper.TABLE_MYLISTCONTENTS, new String[]{"url"}, null, null, null, null, null);
    }

    public Cursor getMyContentsList(int getId) {
        return db.query(DBHelper.TABLE_MYLISTCONTENTS, null, "mylist_id=?", new String[] {Integer.toString(getId)}, null, null, null);
    }

    public void insert(List<ListItem> item) {
        for (ListItem i: item) {
            ContentValues values = new ContentValues();
            values.put("mylist_id", 1);
            values.put("site", i.getSiteName());
            values.put("url", i.getLink());
            db.insertOrThrow(DBHelper.TABLE_MYLISTCONTENTS, null, values);
        }
    }
}