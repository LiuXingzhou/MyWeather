package com.islxz.myweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.islxz.myweather.R;

/**
 * Created by Qingsu on 2017/6/13.
 */

public class AllWeatherFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_weather, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}