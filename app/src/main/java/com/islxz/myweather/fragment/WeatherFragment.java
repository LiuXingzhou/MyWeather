package com.islxz.myweather.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.islxz.myweather.R;
import com.islxz.myweather.gson.Forecast;
import com.islxz.myweather.gson.Weather;
import com.islxz.myweather.util.HttpUrl;
import com.islxz.myweather.util.HttpUtil;
import com.islxz.myweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Qingsu on 2017/6/13.
 */

public class WeatherFragment extends Fragment {

    private TextView mCityTitleTV;
    private TextView mNowTimeTV;
    private TextView mTmpTV;
    private TextView mTxtTV;
    private LinearLayout mHourlyForecastLL;
    private TextView mAQITV;
    private TextView mPM25TV;
    private TextView mComfTV;
    private TextView mCwTV;
    private TextView mSportTV;

    private String mWeatherId;

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mBingPicIV;

    public void initData(String weatherId) {
        mWeatherId = weatherId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, null);
        bindID(view);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mPrefs.edit();
        /*
        下拉刷新
         */
        mSwipeRefreshLayout = view.findViewById(R.id.fm_weather_srl);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        /*
        加载天气信息
         */
        String weatherString = mPrefs.getString("weather", null);
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            if (mWeatherId == weather.basic.weatherId) {
                mWeatherId = weather.basic.weatherId;
                showWeatherInfo(weather);
            }
            //无缓存时去服务器查询天气
            requestWeather(mWeatherId);
        } else {
            //无缓存时去服务器查询天气
            requestWeather(mWeatherId);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
        return view;
    }

    private void bindID(View view) {
        mCityTitleTV = view.findViewById(R.id.fm_weather_tv_city_title);
        mNowTimeTV = view.findViewById(R.id.fm_weather_tv_now_time);
        mTmpTV = view.findViewById(R.id.fm_weather_tv_tmp);
        mTxtTV = view.findViewById(R.id.fm_weather_tv_txt);
        mHourlyForecastLL = view.findViewById(R.id.fm_weather_ll_hourly_forecast);
        mAQITV = view.findViewById(R.id.fm_weather_tv_aqi);
        mPM25TV = view.findViewById(R.id.fm_weather_tv_pm25);
        mComfTV = view.findViewById(R.id.fm_weather_tv_comf);
        mCwTV = view.findViewById(R.id.fm_weather_tv_cw);
        mSportTV = view.findViewById(R.id.fm_weather_tv_sport);
        mSwipeRefreshLayout = view.findViewById(R.id.fm_weather_srl);
        mBingPicIV = getActivity().findViewById(R.id.main_iv);
    }


    /*
    根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId) {
        mSwipeRefreshLayout.setRefreshing(true);
        String weatherUrl = HttpUrl.WEATHER_URL_START + weatherId + HttpUrl.HE_WEATHER_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                Utility.addSelectCounty(weather.basic.cityName, weather.basic.weatherId, weather.now
                        .temperature, weather.now.more.info);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(getContext()).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(getContext(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        mWeatherId = weather.basic.weatherId;
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /*
    处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        mCityTitleTV.setText(cityName);
        mNowTimeTV.setText(updateTime);
        mTmpTV.setText(degree);
        mTxtTV.setText(weatherInfo);
        mHourlyForecastLL.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout
                    .listview_item_hourly_forecast, mHourlyForecastLL, false);
            TextView dateTV = view.findViewById(R.id.listview_item_tv_time);
            TextView infoTV = view.findViewById(R.id.listview_item_tv_txt);
            TextView maxTV = view.findViewById(R.id.listview_item_tv_max);
            TextView minTV = view.findViewById(R.id.listview_item_tv_min);
            dateTV.setText(forecast.date);
            infoTV.setText(forecast.more.info);
            maxTV.setText(forecast.temperature.max);
            minTV.setText(forecast.temperature.min);
            mHourlyForecastLL.addView(view);
        }
        if (weather.aqi != null) {
            mAQITV.setText(weather.aqi.city.aqi);
            mPM25TV.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度指数：" + weather.suggestion.comfort.brfTxt + "（" + weather.suggestion
                .comfort.info + "）";
        String carWash = "洗车指数：" + weather.suggestion.carWash.brfTxt + "（" + weather.suggestion
                .carWash.info + "）";
        String sport = "运动指数：" + weather.suggestion.sport.brfTxt + "（" + weather.suggestion.sport.info
                + "）";
        mComfTV.setText(comfort);
        mCwTV.setText(carWash);
        mSportTV.setText(sport);
    }

    /*
    加载必应每日一图
     */
    public void loadBingPic() {
        String requestBingPic = HttpUrl.BING_PIC;
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                mEditor.putString("bing_pic", bingPic);
                mEditor.apply();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getContext()).load(bingPic).into(mBingPicIV);
                    }
                });
            }
        });
    }
}
