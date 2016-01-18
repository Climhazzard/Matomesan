package com.jobs.matomesan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

public class MyListAdapter extends SimpleCursorAdapter {
    LayoutInflater layoutInflater = null;
    private ViewHolder holder;
    private MyListDBAdapter DBAdapter;

    public MyListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DBAdapter = new MyListDBAdapter(context);
    }

    static class ViewHolder {
        TextView name;
        RadioButton radio;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        holder = (ViewHolder) view.getTag();
        final String name = cursor.getString(cursor.getColumnIndex("name"));
        final int flag = cursor.getInt(cursor.getColumnIndex("flag"));
        holder.name.setText(name);
        boolean rb;
        rb = (flag == 1) ? true : false;
        holder.radio.setChecked(rb);
        holder.radio.setTag(cursor.getPosition());

        holder.radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((RadioButton) view).isChecked()) {
                    int id = (Integer) view.getTag() + 1;
                    MyListDBAdapter DBAdapter = new MyListDBAdapter(context);
                    int getId = DBAdapter.getId(id);
                    ContentValues resetValues = new ContentValues();
                    resetValues.put("flag", 0);
                    context.getContentResolver().update(TestProvider.CONTENT_URI, resetValues, "flag=1", null);
                    ContentValues setValues = new ContentValues();
                    setValues.put("flag", 1);
                    context.getContentResolver().update(TestProvider.CONTENT_URI, setValues, "_id=?", new String[]{Integer.toString(getId)});
                }
            }
        });
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view = layoutInflater.inflate(R.layout.row_mylist, null);
        holder = new ViewHolder();
        holder.name = (TextView) view.findViewById(R.id.title);
        holder.radio = (RadioButton) view.findViewById(R.id.radioButton);
        view.setTag(holder);

        return view;
    }
}