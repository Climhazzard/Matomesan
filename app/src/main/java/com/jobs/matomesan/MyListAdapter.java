package com.jobs.matomesan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class MyListAdapter extends ArrayAdapter<MyListInfo> {
    LayoutInflater layoutInflater = null;
    private int selectedPosition;
    private Context context;

    public MyListAdapter(Context context, List<MyListInfo> objects) {
        super(context, R.layout.row_mylist, objects);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_mylist, parent, false);
            holder = new Holder();
            holder.setTitleText((TextView)convertView.findViewById(R.id.title));
            holder.setCheckBox((CheckBox)convertView.findViewById(R.id.checkBox));
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        MyListDBAdapter DBAdapter = new MyListDBAdapter(context);
        this.selectedPosition = DBAdapter.flagSearch();

        final MyListInfo row = getItem(position);
        holder.getTitleText().setText(row.toString());
        holder.getCheckBox().setTag(position);
        holder.getCheckBox().setChecked(row.isChecked());

        if (position == selectedPosition) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyListDBAdapter DBAdapter = new MyListDBAdapter(context);
                if (((CheckBox)view).isChecked()) {
                    selectedPosition = position;
                    DBAdapter.flagClear();
                    DBAdapter.setFlag(getItem(position).getId(), 1);
                } else {
                    selectedPosition = -1;
                    int id = getItem(position).getId();
                    if (DBAdapter.flagCheck(id)) {
                        holder.checkBox.setChecked(true);
                        return;
                    }
                    DBAdapter.setFlag(getItem(position).getId(), 0);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    static class Holder {
        TextView titleText;
        CheckBox checkBox;

        public TextView getTitleText() {
            return titleText;
        }

        public void setTitleText(TextView titleText) {
            this.titleText = titleText;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
    }
}