package com.jobs.matomesan;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyListContentProvider extends ContentProvider {
    public static final String DATABASE_TABLE = "MyListContents";
    private static final String AUTHORITY = "com.jobs.matomesan.mycontentprovider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + DATABASE_TABLE);
    private DBHelper DBHelper;

    private static final int USERS = 1;
    private static final int USER_ITEM = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher  = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, DATABASE_TABLE, USERS);
        uriMatcher.addURI(AUTHORITY, DATABASE_TABLE + "/#", USER_ITEM);
    }

    @Override
    public boolean onCreate() {
        DBHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(uriMatcher.match(uri) != USERS){
            throw new IllegalArgumentException("Unknown URI: "+ uri);
        }

        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DATABASE_TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        db.insert(DBHelper.TABLE_MYLISTCONTENTS, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        db.delete(DATABASE_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        db.update(DBHelper.TABLE_MYLISTCONTENTS, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }
}
