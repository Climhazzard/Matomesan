package com.jobs.matomesan;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<listItem> listItem;
    private List<listItem> mBackData = new ArrayList<listItem>();
    private LayoutInflater layoutInflater = null;
    private customFilter mFilter;

    public CustomAdapter(Context context, List<listItem> list) {
        super();
        this.context = context;
        this.listItem = list;
        this.listItem = new ArrayList<listItem>(list);
        this.mBackData = list;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(listItem item) {
        listItem.remove(item);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        listItem item = (listItem)getItem(position);
        convertView = layoutInflater.inflate(R.layout.list_item, null);
        TextView tv = (TextView)convertView.findViewById(R.id.list_item);
        TextView tv2 = (TextView)convertView.findViewById(R.id.list_title);
        TextView tv3 = (TextView)convertView.findViewById(R.id.list_date);
        if(position % 2 == 0) {
            convertView.setBackgroundColor(Color.rgb(214, 214, 214));
        } else {
            convertView.setBackgroundColor(Color.rgb(238, 238, 238));
        }
        tv.setText(item.getTitle());
        tv2.setText(item.getSiteName());
        tv3.setText(item.getDate());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new customFilter();
        }
        return mFilter;
    }

    class customFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<listItem> filtered = new ArrayList<>();
            String filterString = constraint.toString().trim().toLowerCase();
            if (TextUtils.isEmpty(filterString)) {
                filtered = mBackData;
            } else {
                for(listItem i : mBackData) {
                    if (i.getTitle().contains(filterString)) {
                        filtered.add(new listItem(i.getSiteName(), i.getTitle(), i.getLink(), i.getDate()));
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            results.count = filtered.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listItem = (List<listItem>)results.values;
            if (results != null && results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}