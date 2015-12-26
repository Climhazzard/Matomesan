package com.jobs.matomesan;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    List<listItem> list = new ArrayList<>();
    public List<listItem> parse(String JsonData) {
        try {
            JSONObject rootObject = new JSONObject(JsonData);
            JSONArray jsonArray = rootObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject js = jsonArray.getJSONObject(i);
                list.add(new listItem(js.getString("site"), js.getString("title"), js.getString("link"), js.getString("date")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
