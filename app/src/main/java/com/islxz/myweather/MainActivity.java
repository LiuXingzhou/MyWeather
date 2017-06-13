package com.islxz.myweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.islxz.myweather.fragment.AllWeatherFragment;
import com.islxz.myweather.fragment.SearchFragment;
import com.islxz.myweather.fragment.WeatherFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private WeatherFragment mWeatherFragment;
    private SearchFragment mSearchFragment;
    private AllWeatherFragment mAllWeatherFragment;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        ifFirstRun();
    }

    private void ifFirstRun() {
        boolean isFirstRun = mSharedPreferences.getBoolean("isfirstrun", true);
        if (isFirstRun) {

            mEditor.putBoolean("isfirstrun", false);
            mEditor.apply();
        } else return;
    }
}
