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

    public void addURL(int id, String url) {
        // urlか正規表現で判定
        // urlがRSSか判定
        // ok =>title, urlをDBに追加
        // NG =>ErrorDialogを表示
        ContentValues values = new ContentValues();
        values.put("mylist_id", id);
        values.put("site", "sitemei");
        values.put("url", url);
        values.put("flag", 1);
        db.insertOrThrow(DBHelper.TABLE_MYLISTCONTENTS, null, values);
    }

    public void insert(List<ListItem> item) {
        for (ListItem i: item) {
            ContentValues values = new ContentValues();
            values.put("mylist_id", 1);
            values.put("site", i.getSiteName());
            values.put("url", i.getLink());
            values.put("flag", 1);
            db.insertOrThrow(DBHelper.TABLE_MYLISTCONTENTS, null, values);
        }
    }

    public void deleteRecode(int id) {
        db.delete(DBHelper.TABLE_MYLISTCONTENTS, "id=?", new String[]{Integer.toString(id)});
    }
}