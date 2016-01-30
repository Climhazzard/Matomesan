package com.jobs.matomesan;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ListView MyListView;
    private EditText editInput;
    private static final String[] from = {"_id", "name", "flag"};
    private static final int[] to = {R.id.title, R.id.radioButton};
    private MyListAdapter adapter;
    MyListInfo item;
    private int poti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.myList);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.articlelist:
                        Intent articleIntent = new Intent(MyListActivity.this, MainActivity.class);
                        articleIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(articleIntent);
                        break;
                    case R.id.mylist:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.history:
                        Intent historyIntent = new Intent(MyListActivity.this, HistoryActivity.class);
                        historyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(historyIntent);
                        break;
                    case R.id.readitlater:
                        Intent readItLaterIntent = new Intent(MyListActivity.this, ReadItLaterActivity.class);
                        readItLaterIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(readItLaterIntent);
                        break;
                    case R.id.bookmark:
                        Intent bookMarkIntent = new Intent(MyListActivity.this, BookMarkActivity.class);
                        bookMarkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(bookMarkIntent);
                        break;
                    case R.id.popularlist:
                        Intent popularIntent = new Intent(MyListActivity.this, PopularActivity.class);
                        popularIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(popularIntent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        List items = new ArrayList<String>();

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from MyList", null);
        MyListView = (ListView) findViewById(android.R.id.list);
        adapter = new MyListAdapter(MyListActivity.this, R.layout.row_mylist, c, from, to, 0);
        MyListView.setAdapter(adapter);
        getLoaderManager().initLoader(0,null,this);

        MyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Cursor c = (Cursor) adapter.getItem(position);
                int getId = c.getInt(c.getColumnIndex("_id"));
                String getName = c.getString(c.getColumnIndex("name"));
                MyListInfo items = new MyListInfo(getId, getName);
                Intent intent = new Intent(MyListActivity.this, MyListContentsActivity.class);
                intent.putExtra("items", items);
                startActivity(intent);
            }
        });

        ContentObserver contentObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                getLoaderManager().restartLoader(0, null, MyListActivity.this);
            }
        };
        getContentResolver().registerContentObserver(TestProvider.CONTENT_URI, true, contentObserver);

        MyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                final Cursor editCursor = (Cursor) adapter.getItem(position);
                poti = editCursor.getPosition();
                final CharSequence[] items = {getResources().getString(R.string.mylist_change_name), getResources().getString(R.string.mylist_delete_list)};
                AlertDialog.Builder listDlg = new AlertDialog.Builder(MyListActivity.this);
                listDlg.setItems(
                        items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        editInput = new EditText(MyListActivity.this);
                                        editInput.setInputType(InputType.TYPE_CLASS_TEXT);
                                        new AlertDialog.Builder(MyListActivity.this)
                                                .setTitle(R.string.add_mylist_dialog)
                                                .setView(editInput)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        MyListDBAdapter DBAdapter = new MyListDBAdapter(MyListActivity.this);
                                                        int getId = DBAdapter.getId2(poti);
                                                        if (getId == 1) {
                                                            new AlertDialog.Builder(MyListActivity.this)
                                                                    .setTitle("Error")
                                                                    .setMessage(R.string.default_mylist_rename_ng)
                                                                    .setPositiveButton("OK", null)
                                                                    .show();
                                                            return;
                                                        }
                                                        ContentValues values = new ContentValues();
                                                        values.put("name", editInput.getText().toString());
                                                        getContentResolver().update(TestProvider.CONTENT_URI, values, "_id=?", new String[]{Integer.toString(getId)});
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .show();
                                        break;
                                    case 1:
                                        new AlertDialog.Builder(MyListActivity.this)
                                                .setMessage(R.string.delete_mylist)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        int getId = editCursor.getInt(editCursor.getColumnIndex("_id"));
                                                        if (getId == 1) {
                                                            new AlertDialog.Builder(MyListActivity.this)
                                                                    .setTitle("Error")
                                                                    .setMessage(R.string.default_mylist_delete_ng)
                                                                    .setPositiveButton("OK", null)
                                                                    .show();
                                                            return;
                                                        }
                                                        int getFlag = editCursor.getInt(editCursor.getColumnIndex("flag"));
                                                        if (getFlag == 0) {
                                                            getContentResolver().delete(TestProvider.CONTENT_URI, "_id=?", new String[]{Integer.toString(getId)});
                                                        } else {
                                                            new AlertDialog.Builder(MyListActivity.this)
                                                                    .setTitle("Error")
                                                                    .setMessage(R.string.delete_ng)
                                                                    .setPositiveButton("OK", null)
                                                                    .show();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .show();
                                        break;
                                }
                            }
                        });
                listDlg.create().show();
                return true;
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, TestProvider.CONTENT_URI, from, null, null, null);
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
        getMenuInflater().inflate(R.menu.menu_my_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_mylist) {
            editInput = new EditText(this);
            editInput.setInputType(InputType.TYPE_CLASS_TEXT);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.add_mylist_dialog)
                    .setView(editInput)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues values = new ContentValues();
                            values.put("name", editInput.getText()
                                    .toString());
                            values.put("flag", 0);
                            getContentResolver().insert(TestProvider.CONTENT_URI, values);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
