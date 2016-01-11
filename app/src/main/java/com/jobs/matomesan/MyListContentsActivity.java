package com.jobs.matomesan;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MyListContentsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Toolbar toolbar;
    private EditText editInput;
    MyListInfo getItems;
    Cursor cursor;
    private static final String[] from = {"_id", "mylist_id", "site", "flag"};
    private static final int[] to = {R.id.text, R.id.check};
    private MyListContentsAdapter adapter;
    private String url_pattern = "https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_contents);

        Intent intent = getIntent();
        getItems = (MyListInfo)intent.getSerializableExtra("items");
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getItems.toString());

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from MyListContents", null);
        adapter = new MyListContentsAdapter(MyListContentsActivity.this, R.layout.row_mylistcontents, c, from, to, 0);

        ListView mListView = (ListView) findViewById(android.R.id.list);
        mListView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Cursor c = (Cursor) adapter.getItem(position);
                CheckBox ck = (CheckBox) view.findViewById(R.id.check);
                ContentValues values = new ContentValues();
                if (c.getInt(c.getColumnIndex("flag")) == 1) {
                    ck.setChecked(false);
                    int tmp_id = c.getInt(c.getColumnIndex("_id"));
                    values.put("flag", 0);
                    getContentResolver().update(MyListContentProvider.CONTENT_URI, values, "_id=?", new String[]{Integer.toString(tmp_id)});
                } else {
                    ck.setChecked(true);
                    int tmp_id = c.getInt(c.getColumnIndex("_id"));
                    values.put("flag", 1);
                    getContentResolver().update(MyListContentProvider.CONTENT_URI, values, "_id=?", new String[]{Integer.toString(tmp_id)});
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                cursor = (Cursor) adapter.getItem(position);
                new AlertDialog.Builder(MyListContentsActivity.this)
                        .setMessage(R.string.delete_url)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int tmp_id = cursor.getInt(cursor.getColumnIndex("_id"));
                                getContentResolver().delete(MyListContentProvider.CONTENT_URI, "_id=?", new String[]{Integer.toString(tmp_id)});
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

                return true;
            }
        });

        ContentObserver contentObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                getLoaderManager().restartLoader(0, null, MyListContentsActivity.this);
            }
        };
        getContentResolver().registerContentObserver(MyListContentProvider.CONTENT_URI, true, contentObserver);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MyListContentProvider.CONTENT_URI, from, "mylist_id=?", new String[]{Integer.toString(getItems.getId())}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor>loader, Cursor cursor) {
        adapter.swapCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_list_contents, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_url) {
            editInput = new EditText(this);
            editInput.setInputType(InputType.TYPE_CLASS_TEXT);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.add_input_url)
                    .setView(editInput)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (editInput.getText().toString().matches(url_pattern)) {
                                AsyncThread at = new AsyncThread();
                                at.feedUrlCheck(getItems.getId(), editInput.getText().toString());
                                at.setCallBack(new AsyncThread.CallBack() {
                                    @Override
                                    public void onProgressUpdate(String[] item) {
                                        if (item[0].equals("error")) {
                                            new AlertDialog.Builder(MyListContentsActivity.this)
                                                    .setTitle("Error")
                                                    .setMessage(R.string.error_url)
                                                    .setPositiveButton("OK", null)
                                                    .show();
                                        } else {
                                            ContentValues values = new ContentValues();
                                            values.put("mylist_id", getItems.getId());
                                            values.put("site", item[0]);
                                            values.put("url", item[1]);
                                            values.put("flag", 1);
                                            getContentResolver().insert(MyListContentProvider.CONTENT_URI, values);
                                            Toast.makeText(MyListContentsActivity.this, R.string.finish_add_url, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                new AlertDialog.Builder(MyListContentsActivity.this)
                                        .setTitle("Error")
                                        .setMessage(R.string.error_url)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
