package com.jobs.matomesan;

import android.content.Context;
import android.database.Cursor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MyListContentsAdapter extends SimpleCursorAdapter {
    private LayoutInflater layoutInflater = null;
    private Context context;
    private ViewHolder holder;

    MyListContentsDBAdapter DBAdapter;

    static class ViewHolder {
        TextView site;
        CheckBox cb;
    }

    public MyListContentsAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DBAdapter = new MyListContentsDBAdapter(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        holder = (ViewHolder) view.getTag();
        final String site = cursor.getString(cursor.getColumnIndex("site"));
        final int flag = cursor.getInt(cursor.getColumnIndex("flag"));
        holder.site.setText(site);
        boolean cb;
        cb = (flag == 1) ? true : false;
        holder.cb.setChecked(cb);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view = layoutInflater.inflate(R.layout.row_mylistcontents, null);
        holder = new ViewHolder();
        holder.site = (TextView) view.findViewById(R.id.row_textView);
        holder.cb = (CheckBox) view.findViewById(R.id.check);
        view.setTag(holder);

        return view;
    }
}
