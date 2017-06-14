package com.islxz.myweather.util;

import android.content.ContentValues;

import com.google.gson.Gson;
import com.islxz.myweather.db.SelectCounty;
import com.islxz.myweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qingsu on 2017/6/12.
 */

public class Utility {

    /*
    添加数据到数据库
     */
    public static boolean addSelectCounty(String CountyName, String WeatherId, String info, String tmp) {
        if (!isExist(CountyName)) {
            SelectCounty selectCounty = new SelectCounty();
            selectCounty.setSelectCountyId(0);
            selectCounty.setCountyName(CountyName);
            selectCounty.setWeatherId(WeatherId);
            selectCounty.setInfo(info);
            selectCounty.setTmp(tmp);
            selectCounty.save();
            return true;
        } else {
            return false;
        }
    }

    /*
    更新数据库
     */
    public static boolean updateSelectCounty(String CountyName, String WeatherId, String info, String
            tmp) {
        SelectCounty selectCounty = new SelectCounty();
        selectCounty.setCountyName(CountyName);
        selectCounty.setInfo(info);
        selectCounty.setTmp(tmp);
        selectCounty.updateAll("countyName = ?", CountyName);
        return true;
    }

    /*
    判断数据库中是否存在
     */
    public static boolean isExist(String CountyName) {
        List<SelectCounty> selectCountyList = DataSupport.where("countyName = ?", CountyName).find
                (SelectCounty
                        .class);
        if (selectCountyList.size() > 0) {
            return true;
        }
        return false;
    }

    /*
    将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    将返回的查询JSON数据解析
     */
    public static List<ContentValues> handleSearchResponse(String response) {
        List<ContentValues> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            list.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONObject basicObject = jsonObject1.getJSONObject("basic");
                String city = basicObject.getString("city");
                String id = basicObject.getString("id");
                ContentValues values = new ContentValues();
                values.put("city", city);
                values.put("id", id);
                list.add(values);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}