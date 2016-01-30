package com.jobs.matomesan;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Filterable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.timroes.android.listview.EnhancedListView;


public class PopularActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    List<ListItem> list = new ArrayList<>();
    private EnhancedListView listView;
    private SwipeRefreshLayout mSwipe;
    private ListItem bookMarkItem;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.popularList);
        listView = (EnhancedListView) findViewById(android.R.id.list);
        mSwipe = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        mSwipe.setColorSchemeResources(R.color.base_color, R.color.blue, R.color.red, R.color.green);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.first_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipe.setRefreshing(true);
                AsyncThread task = new AsyncThread(listView, PopularActivity.this, mSwipe);
                task.popularGet();
            }
        });

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
                        Intent articleIntent = new Intent(PopularActivity.this, MainActivity.class);
                        articleIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(articleIntent);
                        break;
                    case R.id.mylist:
                        Intent mylistIntent = new Intent(PopularActivity.this, MyListActivity.class);
                        mylistIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mylistIntent);
                        break;
                    case R.id.popularlist:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.history:
                        Intent historyIntent = new Intent(PopularActivity.this, HistoryActivity.class);
                        historyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(historyIntent);
                        break;
                    case R.id.bookmark:
                        Intent bookMarkIntent = new Intent(PopularActivity.this, BookMarkActivity.class);
                        bookMarkIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(bookMarkIntent);
                        break;
                    case R.id.readitlater:
                        Intent readItLaterIntent = new Intent(PopularActivity.this, ReadItLaterActivity.class);
                        readItLaterIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(readItLaterIntent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        mSwipe.post(new Runnable() {
            @Override
            public void run() {
                mSwipe.setRefreshing(true);
            }
        });

        AsyncThread task = new AsyncThread(listView, PopularActivity.this, mSwipe);
        task.popularGet();

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncThread task = new AsyncThread(listView, PopularActivity.this, mSwipe);
                task.popularGet();
            }
        });

        listView.setDismissCallback(new de.timroes.android.listview.EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {
                CustomPopularAdapter customPopularAdapter = (CustomPopularAdapter) listView.getAdapter();
                ListItem item = (ListItem) customPopularAdapter.getItem(position);
                customPopularAdapter.remove(item);
                customPopularAdapter.notifyDataSetChanged();

                ReadItLaterDBAdapter DBAdapter = new ReadItLaterDBAdapter(PopularActivity.this);
                DBAdapter.insert(item);

                Toast.makeText(PopularActivity.this, R.string.read_later, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
        listView.enableSwipeToDismiss();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                CustomPopularAdapter adapter = (CustomPopularAdapter) parent.getAdapter();
                bookMarkItem = (ListItem) adapter.getItem(position);
                new AlertDialog.Builder(PopularActivity.this)
                        .setMessage(R.string.add_bookmark)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BookMarkDBAdapter DBAdapter = new BookMarkDBAdapter(PopularActivity.this);
                                DBAdapter.insert(bookMarkItem);
                                Toast.makeText(PopularActivity.this, R.string.complete_add_bookmark, Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint(getString(R.string.search_hint));
        listView.setTextFilterEnabled(true);
        int options = mSearchView.getImeOptions();
        mSearchView.setImeOptions(options| EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String str) {
                if (listView.getAdapter() == null) {
                    return false;
                }
                android.widget.Filter filter = ((Filterable) listView.getAdapter()).getFilter();
                if (TextUtils.isEmpty(str)) {
                    //listView.clearTextFilter();
                    filter.filter("");
                } else {
                    //listView.setFilterText(str);
                    filter.filter(str);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
