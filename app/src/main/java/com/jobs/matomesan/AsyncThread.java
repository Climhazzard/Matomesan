package com.jobs.matomesan;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(v[0]);
        ArrayList value = new ArrayList<NameValuePair>();
        value.add(new BasicNameValuePair("str", "http://hamusoku.com/index.rdf,http://blog.livedoor.jp/dqnplus/index.rdf,http://alfalfalfa.com/index.rdf,http://blog.livedoor.jp/kinisoku/index.rdf,http://himasoku.com/index.rdf"));
        String json = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(value, "UTF-8"));
            HttpResponse res = httpClient.execute(post);
            HttpEntity entity = res.getEntity();
            json = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onPostExecute(String result) {
        JsonParser jp = new JsonParser();
        List<listItem> list = jp.parse(result);
        listView.setAdapter(new CustomAdapter(context, list));
        if (mSwipe != null) mSwipe.setRefreshing(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                listItem item = (listItem) parent.getAdapter().getItem(position);
                DBAdapter DBAdapter = new DBAdapter(context);
                DBAdapter.insert(item);
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("getLink", item.getLink());
                context.startActivity(intent);
            }
        });
    }
}
