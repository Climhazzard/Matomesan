package com.jobs.matomesan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
    private Context context;

    public MyListDBAdapter(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
        this.context = context;
    }

    public void addDefaultMyList() {
        ContentValues values = new ContentValues();
        values.put("name", context.getString(R.string.default_mylist_name));
        values.put("flag", 1);
        db.insertOrThrow(DBHelper.TABLE_MYLIST, null, values);
    }

    public void addNewMyList(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("flag", 0);
        db.insertOrThrow(DBHelper.TABLE_MYLIST, null, values);
    }

    public Cursor getLatestMyList(String name) {
        return db.query(DBHelper.TABLE_MYLIST, null, "name=?", new String[] {name}, null, null, null);
    }

    public Cursor getMyList() {
        return db.query(DBHelper.TABLE_MYLIST, new String[]{"id", "name", "flag"}, null, null, null, null, null);
    }

    public void setFlag(int id, int flag) {
        ContentValues values = new ContentValues();
        values.put("flag", flag);
        db.update(DBHelper.TABLE_MYLIST, values, "id=?", new String[] {Integer.toString(id)});
    }

    public boolean flagCheck(int id) {
        long recodeCount = DatabaseUtils.queryNumEntries(db, DBHelper.TABLE_MYLIST, "id=? AND flag=1", new String[]{Integer.toString(id)});
        return (recodeCount == 1) ? true : false;
    }

    public void flagClear() {
        ContentValues values = new ContentValues();
        values.put("flag", 0);
        db.update(DBHelper.TABLE_MYLIST, values, null, null);
    }

    public int flagSearch() {
        int id = 0;
        Cursor c = db.query(DBHelper.TABLE_MYLIST, null, "flag=?", new String[] {"1"}, null, null, null);
        while (c.moveToNext()) {
            id = c.getInt(c.getColumnIndex("id"));
        }
        return id - 1;
    }
}