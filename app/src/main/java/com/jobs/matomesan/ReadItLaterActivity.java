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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.timroes.android.listview.EnhancedListView;


public class ReadItLaterActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    List<ListItem> list = new ArrayList<>();
    private EnhancedListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_it_later);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.readItLater);

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
                        Intent articleIntent = new Intent(ReadItLaterActivity.this, MainActivity.class);
                        articleIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(articleIntent);
                        break;
                    case R.id.mylist:
                        Intent mylistIntent = new Intent(ReadItLaterActivity.this, MyListActivity.class);
                        mylistIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mylistIntent);
                        break;
                    case R.id.readitlater:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.history:
                        Intent historyIntent = new Intent(ReadItLaterActivity.this, HistoryActivity.class);
                        historyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(historyIntent);
                        break;
                    case R.id.bookmark:
                        Intent bookMarkIntent = new Intent(ReadItLaterActivity.this, BookMarkActivity.class);
                        bookMarkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(bookMarkIntent);
                        break;
                    case R.id.popularlist:
                        Intent popularIntent = new Intent(ReadItLaterActivity.this, PopularActivity.class);
                        popularIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(popularIntent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        ReadItLaterDBAdapter DBAdapter = new ReadItLaterDBAdapter(this);
        Cursor c = DBAdapter.getAllList();
        while (c.moveToNext()) {
            list.add(new ListItem(c.getInt(c.getColumnIndex("_id")),
                    c.getString(c.getColumnIndex("site")),
                    c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("url")),
                    c.getString(c.getColumnIndex("date"))));
        }
        listView.setAdapter(new CustomAdapter(ReadItLaterActivity.this, list));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                CustomAdapter customAdapter = (CustomAdapter) parent.getAdapter();
                ListItem item = (ListItem) customAdapter.getItem(position);
                ReadItLaterDBAdapter DBAdapter = new ReadItLaterDBAdapter(ReadItLaterActivity.this);
                DBAdapter.deleteRecode(item.getId());

                HistoryDBAdapter historyDBAdapter = new HistoryDBAdapter(ReadItLaterActivity.this);
                historyDBAdapter.insert(item);

                Intent intent = new Intent(ReadItLaterActivity.this, WebViewActivity.class);
                intent.putExtra("getLink", item.getLink());
                startActivity(intent);
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

                ReadItLaterDBAdapter DBAdapter = new ReadItLaterDBAdapter(ReadItLaterActivity.this);
                DBAdapter.deleteRecode(item.getId());

                customAdapter.notifyDataSetChanged();
                Toast.makeText(ReadItLaterActivity.this, R.string.readItLater_delete, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
        listView.enableSwipeToDismiss();
    }

    @Override
    protected void onStop() {
        super.onPause();
        ReadItLaterDBAdapter DBAdapter = new ReadItLaterDBAdapter(ReadItLaterActivity.this);
        List<ListItem> list = new ArrayList<>();
        Cursor c = DBAdapter.getAllList();
        while (c.moveToNext()) {
            list.add(new ListItem(c.getInt(c.getColumnIndex("_id")),
                    c.getString(c.getColumnIndex("site")),
                    c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("url")),
                    c.getString(c.getColumnIndex("date"))));
        }
        listView.setAdapter(new CustomAdapter(ReadItLaterActivity.this, list));
        ((CustomAdapter)listView.getAdapter()).notifyDataSetChanged();
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
            new AlertDialog.Builder(ReadItLaterActivity.this)
                    .setMessage(R.string.trash)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listView.setAdapter(new CustomAdapter(ReadItLaterActivity.this, new ArrayList<ListItem>()));
                            ((CustomAdapter)listView.getAdapter()).notifyDataSetChanged();
                            ReadItLaterDBAdapter DBAdapter = new ReadItLaterDBAdapter(ReadItLaterActivity.this);
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
