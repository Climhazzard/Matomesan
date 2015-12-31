package com.jobs.matomesan;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ListView MyListView;
    private EditText editInput;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.myList);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
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


        NavigationView mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
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
                    default:
                        break;
                }
                return false;
            }
        });
        SharedPreferences data = getSharedPreferences("DataSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        List items = new ArrayList<String>();
        if (data.getInt("init_mylist", 0) == 0) {
            MyListDBAdapter DBAdapter = new MyListDBAdapter(MyListActivity.this);
            DBAdapter.addDefaultMyList();
            editor.putInt("init_mylist", 1);
            editor.commit();
        }

        MyListDBAdapter DBAdapter = new MyListDBAdapter(MyListActivity.this);
        Cursor c = DBAdapter.getMyList();
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));
            String name = c.getString(c.getColumnIndex("name"));
            items.add(new MyListInfo(id, name));
        }
        MyListView = (ListView)findViewById(android.R.id.list);
        adapter = new ArrayAdapter<String>(this, R.layout.row_mylist, R.id.row_textView, items);
        MyListView.setAdapter(adapter);

        MyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                MyListInfo items = (MyListInfo)parent.getAdapter().getItem(position);
                Intent intent = new Intent(MyListActivity.this, MyListContentsActivity.class);
                intent.putExtra("items", items);
                startActivity(intent);
            }
        });
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
            new AlertDialog.Builder(this)
                    .setTitle(R.string.add_mylist_dialog)
                    .setView(editInput)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyListDBAdapter DBAdapter = new MyListDBAdapter(MyListActivity.this);
                            DBAdapter.addNewMyList(editInput.getText().toString());
                            List items = new ArrayList<String>();
                            Cursor c = DBAdapter.getMyList();
                            while (c.moveToNext()) {
                                int id = c.getInt(c.getColumnIndex("id"));
                                String name = c.getString(c.getColumnIndex("name"));
                                items.add(new MyListInfo(id, name));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyListActivity.this, R.layout.row_mylist, R.id.row_textView, items);
                            MyListView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
