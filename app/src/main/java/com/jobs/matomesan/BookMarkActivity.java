package com.jobs.matomesan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.timroes.android.listview.EnhancedListView;


public class BookMarkActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ListView listView;
    List<ListItem> list = new ArrayList<>();
    CustomAdapter adapter;
    ListItem item;
    private int tmpId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.bookMark);

        listView = (EnhancedListView) findViewById(android.R.id.list);

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
                        Intent articleIntent = new Intent(BookMarkActivity.this, MainActivity.class);
                        articleIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(articleIntent);
                        break;
                    case R.id.mylist:
                        Intent mylistIntent = new Intent(BookMarkActivity.this, MyListActivity.class);
                        mylistIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mylistIntent);
                        break;
                    case R.id.readitlater:
                        Intent readItLaterIntent = new Intent(BookMarkActivity.this, ReadItLaterActivity.class);
                        readItLaterIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(readItLaterIntent);
                        break;
                    case R.id.history:
                        Intent historyIntent = new Intent(BookMarkActivity.this, HistoryActivity.class);
                        historyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(historyIntent);
                        break;
                    case R.id.bookmark:
                        drawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        BookMarkDBAdapter DBAdapter = new BookMarkDBAdapter(this);
        Cursor c = DBAdapter.getAllList();
        while (c.moveToNext()) {
            list.add(new ListItem(c.getInt(c.getColumnIndex("_id")),
                    c.getString(c.getColumnIndex("site")),
                    c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("url")),
                    c.getString(c.getColumnIndex("date"))));
        }
        listView.setAdapter(new CustomAdapter(BookMarkActivity.this, list));

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                adapter = (CustomAdapter) parent.getAdapter();
                item = (ListItem) adapter.getItem(position);
                tmpId = item.getId();
                new AlertDialog.Builder(BookMarkActivity.this)
                        .setMessage(R.string.delete)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.remove(item);
                                adapter.notifyDataSetChanged();
                                BookMarkDBAdapter DBAdapter = new BookMarkDBAdapter(BookMarkActivity.this);
                                DBAdapter.deleteRecode(tmpId);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_trash) {
            new AlertDialog.Builder(BookMarkActivity.this)
                    .setMessage(R.string.trash)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listView.setAdapter(new CustomAdapter(BookMarkActivity.this, new ArrayList<ListItem>()));
                            ((CustomAdapter)listView.getAdapter()).notifyDataSetChanged();
                            BookMarkDBAdapter DBAdapter = new BookMarkDBAdapter(BookMarkActivity.this);
                            DBAdapter.deleteAllRecode();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
