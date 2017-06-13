package com.islxz.myweather.util;

/**
 * Created by Qingsu on 2017/6/13.
 */

public class HttpUrl {
    /*
    中国所有省份名称及省份id
    城市：http://guolin.tech/api/china/省份id
    县区：http://guolin.tech/api/china/省份id/市区id
     */
    public static final String CHINA_BASE_URL = "http://guolin.tech/api/china/";
    /*
    获取天气信息：http://guolin.tech/api/weather?cityid=&key=
    cityid=市区id
    key=0ca11df91fec4fa5bb7fea6d53aeb015
     */
    public static final String WEATHER_URL_START = "http://guolin.tech/api/weather?cityid=";
    public static final String WEATHER_URL_END = "&key=0ca11df91fec4fa5bb7fea6d53aeb015";
    /*
    获取必应每日一图的借口
     */
    public static final String BING_PIC = "http://guolin.tech/api/bing_pic";
}
