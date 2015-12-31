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
        values.put("created_at", "a");
        db.insertOrThrow(DBHelper.TABLE_MYLIST, null, values);
    }

    public void addNewMyList(String name) {
        //long recodeCount = DatabaseUtils.queryNumEntries(db, DBHelper.TABLE_MYLIST, "name=?", new String[]{name});
        //Log.d("long", recodeCount + "a");
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("created_at", "a");
        db.insertOrThrow(DBHelper.TABLE_MYLIST, null, values);
    }

    public Cursor getLatestMyList(String name) {
        return db.query(DBHelper.TABLE_MYLIST, null, "name=?", new String[] {name}, null, null, null);
    }

    public Cursor getMyList() {
        return db.query(DBHelper.TABLE_MYLIST, new String[]{"id", "name"}, null, null, null, null, null);
    }
}