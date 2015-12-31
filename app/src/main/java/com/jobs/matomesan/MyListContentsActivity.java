package com.jobs.matomesan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MyListContentsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView MyListContentsView;
    private EditText editInput;
    ArrayAdapter<String> adapter;
    MyListInfo getItems;
    MyListInfo items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_contents);

        Intent intent = getIntent();
        getItems = (MyListInfo)intent.getSerializableExtra("items");
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getItems.toString());

        List listItems = new ArrayList<String>();
        MyListContentsDBAdapter DBAdapter = new MyListContentsDBAdapter(MyListContentsActivity.this);
        Cursor c = DBAdapter.getMyContentsList(getItems.getId());
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));
            String name = c.getString(c.getColumnIndex("site"));
            listItems.add(new MyListInfo(id, name));
        }
        MyListContentsView = (ListView)findViewById(android.R.id.list);
        adapter = new ArrayAdapter<String>(this, R.layout.row_mylistcontents, R.id.row_textView, listItems);
        MyListContentsView.setAdapter(adapter);

        MyListContentsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                items = (MyListInfo)parent.getAdapter().getItem(position);
                new AlertDialog.Builder(MyListContentsActivity.this)
                        .setMessage(R.string.delete)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyListContentsDBAdapter DBAdapter = new MyListContentsDBAdapter(MyListContentsActivity.this);
                                DBAdapter.deleteRecode(items.getId());
                                Cursor c = DBAdapter.getMyContentsList(getItems.getId());
                                List listItems = new ArrayList<String>();
                                while (c.moveToNext()) {
                                    int id = c.getInt(c.getColumnIndex("id"));
                                    String name = c.getString(c.getColumnIndex("site"));
                                    listItems.add(new MyListInfo(id, name));
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyListContentsActivity.this, R.layout.row_mylistcontents, R.id.row_textView, listItems);
                                MyListContentsView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
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
            new AlertDialog.Builder(this)
                    .setTitle(R.string.add_input_url)
                    .setView(editInput)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyListContentsDBAdapter DBAdapter = new MyListContentsDBAdapter(MyListContentsActivity.this);
                            DBAdapter.addURL(getItems.getId(), editInput.getText().toString());
                            Cursor c = DBAdapter.getMyContentsList(getItems.getId());
                            List listItems = new ArrayList<String>();
                            while (c.moveToNext()) {
                                int id = c.getInt(c.getColumnIndex("id"));
                                String name = c.getString(c.getColumnIndex("site"));
                                listItems.add(new MyListInfo(id, name));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyListContentsActivity.this, R.layout.row_mylistcontents, R.id.row_textView, listItems);
                            MyListContentsView.setAdapter(adapter);
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
