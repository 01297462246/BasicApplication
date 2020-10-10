package com.example.basicapplication.parse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonParser {
    private HashMap<String, String> parseJsonObject(JSONObject jso){
        HashMap<String,String> dataList = new HashMap<>();
        try {
            String title = jso.getString("title");
            String address = jso.getString("address");
            String category = jso.getString("category");
            String img = jso.getString("img");
            String img_big = jso.getString("img_big");
            String latitude = jso.getJSONObject("gps").getString("latitude").trim();
            String longitude = jso.getJSONObject("gps").getString("longitude").trim();
            String description = jso.getString("description");
            String rating = jso.getString("rating");
            String id = jso.getString("hash");
            dataList.put("title",title);
            dataList.put("address",address);
            dataList.put("category",category);
            dataList.put("img",img);
            dataList.put("img_big",img_big);
            dataList.put("description",description);
            dataList.put("rating",rating);
            dataList.put("latitude",latitude);
            dataList.put("longitude",longitude);
            dataList.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }
    private ArrayList<HashMap<String,String>> parseJsonArray(JSONArray jsonArray){
        ArrayList<HashMap<String,String>> dataList = new ArrayList<>();
        dataList.clear();
        for (int i=0;i<jsonArray.length();i++){
            try {
                HashMap<String, String> data = parseJsonObject((JSONObject)jsonArray.get(i));
                dataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }
    public ArrayList<HashMap<String,String>> parseResult(JSONObject object){
        JSONArray jsonArray = null;
        //get result array
        try{
            jsonArray = object.getJSONObject("result").getJSONArray("poi");
//            System.out.println("RESULT: "+jsonArray);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return parseJsonArray(jsonArray);
    }
}
