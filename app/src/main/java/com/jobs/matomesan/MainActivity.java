package com.jobs.matomesan;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String XML_PARSER_URL = "http://testmode.s348.xrea.com/xmlparser.php";
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout mSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView)findViewById(android.R.id.list);
        mSwipe = (SwipeRefreshLayout)findViewById(R.id.swipelayout);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.first_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncThread task = new AsyncThread(listView, MainActivity.this);
                task.execute(XML_PARSER_URL);
                Toast.makeText(MainActivity.this, "now loading...", Toast.LENGTH_SHORT).show();
            }
        });

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
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.mylist:
                        Toast.makeText(MainActivity.this, "mylist", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.history:
                        Toast.makeText(MainActivity.this, "history", Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
                return false;
            }
        });

        AsyncThread task = new AsyncThread(listView, MainActivity.this);
        task.execute(XML_PARSER_URL);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncThread task = new AsyncThread(listView, MainActivity.this, mSwipe);
                task.execute(XML_PARSER_URL);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}