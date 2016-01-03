package com.jobs.matomesan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import javax.security.auth.callback.Callback;

public class MyListContentsDBAdapter implements Callback {
    private SQLiteDatabase db;
    private DBHelper helper;
    public static final String DATABASE_TABLE = "MyListContents";
    private String url_pattern = "https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+";

    public MyListContentsDBAdapter(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public Cursor getURLList() {
        //return db.query(DBHelper.TABLE_MYLISTCONTENTS, new String[]{"url"}, null, null, null, null, null);
        return db.rawQuery("select MyListContents.url from MyList inner join MyListContents on MyList.id = MyListContents.mylist_id where MyList.flag = 1 and MyListContents.flag = 1", null);
    }

    public Cursor getMyContentsList(int getId) {
        return db.query(DBHelper.TABLE_MYLISTCONTENTS, null, "mylist_id=?", new String[] {Integer.toString(getId)}, null, null, null);
    }

    public boolean addURL(int id, String url) {
        if (url.matches(url_pattern)) {
            AsyncThread at = new AsyncThread();
            at.feedUrlCheck(id, url);
        } else {
            return false;
        }
        return true;
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