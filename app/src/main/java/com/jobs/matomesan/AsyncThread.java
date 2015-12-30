package com.jobs.matomesan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class AsyncThread extends AsyncTask<String, String, String> {
    private ListView listView;
    private Context context;
    private SwipeRefreshLayout mSwipe;
    private StringBuilder reqURL;

    public AsyncThread(ListView listView, Context context) {
        this.listView = listView;
        this.context = context;
    }

    public AsyncThread(ListView listView, Context context, SwipeRefreshLayout mmSwipe) {
        this.listView = listView;
        this.context = context;
        this.mSwipe = mmSwipe;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... v) {
        SharedPreferences data = context.getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        if (data.getInt("init", 0) == 0) {
            initGetMyList(v[0]);
            editor.putInt("init", 1);
            editor.commit();
        }

        MyListContentsDBAdapter DBAdapter = new MyListContentsDBAdapter(context);
        Cursor c = DBAdapter.getURLList();
        reqURL = new StringBuilder();
        while (c.moveToNext()) {
            reqURL.append(c.getString(c.getColumnIndex("url"))).append(",");
        }
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(v[0]);
        ArrayList value = new ArrayList<NameValuePair>();
        value.add(new BasicNameValuePair("str", reqURL.substring(0, reqURL.length() - 1).toString()));
        String json = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(value, "UTF-8"));
            HttpResponse res = httpClient.execute(post);
            HttpEntity entity = res.getEntity();
            json = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpClient.getConnectionManager().shutdown();
        return json;
    }

    @Override
    protected void onPostExecute(String result) {
        JsonParser jp = new JsonParser();
        List<ListItem> list = jp.parse(result);
        listView.setAdapter(new CustomAdapter(context, list));
        if (mSwipe != null) mSwipe.setRefreshing(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                ListItem item = (ListItem)parent.getAdapter().getItem(position);
                HistoryDBAdapter DBAdapter = new HistoryDBAdapter(context);
                DBAdapter.insert(item);
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("getLink", item.getLink());
                context.startActivity(intent);
            }
        });
    }

    public void initGetMyList(String v) {
        String json = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet iniReq = new HttpGet(v);
            HttpResponse iniRes = httpClient.execute(iniReq);
            HttpEntity iniEntity = iniRes.getEntity();
            json = EntityUtils.toString(iniEntity, "UTF-8");
            JsonParser jp = new JsonParser();
            List<ListItem> list = jp.init(json);
            MyListContentsDBAdapter DBAdapter = new MyListContentsDBAdapter(context);
            DBAdapter.insert(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
