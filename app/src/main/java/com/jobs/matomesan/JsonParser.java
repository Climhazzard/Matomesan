package com.jobs.matomesan;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    List<ListItem> list = new ArrayList<>();

    public List<ListItem> init(String JsonData) {
        try {
            JSONObject rootObject = new JSONObject(JsonData);
            JSONArray jsonArray = rootObject.getJSONArray("init");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject js = jsonArray.getJSONObject(i);
                list.add(new ListItem(js.getString("site"), js.getString("link")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ListItem> parse(String JsonData) {
        try {
            JSONObject rootObject = new JSONObject(JsonData);
            JSONArray jsonArray = rootObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject js = jsonArray.getJSONObject(i);
                list.add(new ListItem(js.getString("site"), js.getString("title"), js.getString("link"), js.getString("date")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String[] addUrl(String JsonData) {
        String[] item = new String[2];
        try {
            JSONObject rootObject = new JSONObject(JsonData);
            JSONArray jsonArray = rootObject.getJSONArray("feed");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject js = jsonArray.getJSONObject(i);
                item[0] = js.getString("site");
                item[1] = js.getString("link");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }
}
