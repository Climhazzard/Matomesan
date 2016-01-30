package com.jobs.matomesan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.timroes.android.listview.EnhancedListView;


public class HistoryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    List<ListItem> list = new ArrayList<>();
    private EnhancedListView listView;
    private DrawerLayout drawerLayout;
    CustomAdapter adapter;
    ListItem item;
    private ListItem bookMarkItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.history);

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
                        Intent articleIntent = new Intent(HistoryActivity.this, MainActivity.class);
                        articleIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(articleIntent);
                        break;
                    case R.id.mylist:
                        Intent mylistIntent = new Intent(HistoryActivity.this, MyListActivity.class);
                        mylistIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mylistIntent);
                        break;
                    case R.id.history:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.readitlater:
                        Intent readItLaterIntent = new Intent(HistoryActivity.this, ReadItLaterActivity.class);
                        readItLaterIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(readItLaterIntent);
                        break;
                    case R.id.bookmark:
                        Intent bookMarkIntent = new Intent(HistoryActivity.this, BookMarkActivity.class);
                        bookMarkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(bookMarkIntent);
                        break;
                    case R.id.popularlist:
                        Intent popularIntent = new Intent(HistoryActivity.this, PopularActivity.class);
                        popularIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(popularIntent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        HistoryDBAdapter DBAdapter = new HistoryDBAdapter(this);
        Cursor c = DBAdapter.getAllList();
        while (c.moveToNext()) {
            list.add(new ListItem(c.getInt(c.getColumnIndex("_id")),
                    c.getString(c.getColumnIndex("site")),
                    c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("url")),
                    c.getString(c.getColumnIndex("date"))));
        }
        listView.setAdapter(new CustomAdapter(HistoryActivity.this, list));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                ListItem item = (ListItem) parent.getAdapter().getItem(position);
                Intent intent = new Intent(HistoryActivity.this, WebViewActivity.class);
                intent.putExtra("getLink", item.getLink());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                CustomAdapter adapter = (CustomAdapter) parent.getAdapter();
                bookMarkItem = (ListItem) adapter.getItem(position);
                new AlertDialog.Builder(HistoryActivity.this)
                        .setMessage(R.string.add_bookmark)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BookMarkDBAdapter DBAdapter = new BookMarkDBAdapter(HistoryActivity.this);
                                DBAdapter.insert(bookMarkItem);
                                Toast.makeText(HistoryActivity.this, R.string.complete_add_bookmark, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

                return true;
            }
        });

        listView.setDismissCallback(new de.timroes.android.listview.EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {
                CustomAdapter customAdapter = (CustomAdapter) listView.getAdapter();
                if (customAdapter.getCount() <= position) {
                    return null;
                }
                ListItem item = (ListItem) customAdapter.getItem(position);
                customAdapter.remove(item);

                HistoryDBAdapter DBAdapter = new HistoryDBAdapter(HistoryActivity.this);
                DBAdapter.deleteRecode(item.getId());

                customAdapter.notifyDataSetChanged();
                Toast.makeText(HistoryActivity.this, R.string.readItLater_delete, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
        listView.enableSwipeToDismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        toolbar.inflateMenu(R.menu.trash);
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
            new AlertDialog.Builder(HistoryActivity.this)
                    .setMessage(R.string.trash)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listView.setAdapter(new CustomAdapter(HistoryActivity.this, new ArrayList<ListItem>()));
                            ((CustomAdapter)listView.getAdapter()).notifyDataSetChanged();
                            HistoryDBAdapter DBAdapter = new HistoryDBAdapter(HistoryActivity.this);
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
