package com.islxz.myweather.fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.islxz.myweather.MainActivity;
import com.islxz.myweather.R;
import com.islxz.myweather.util.HttpUrl;
import com.islxz.myweather.util.HttpUtil;
import com.islxz.myweather.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Qingsu on 2017/6/13.
 */

public class SearchFragment extends Fragment implements View.OnClickListener {

    private ImageView mBackIV;
    private EditText mEditText;
    private ImageView mSearchIV;
    private ListView mListView;

    private List<String> mStringList;
    private List<ContentValues> mContentValues;

    private ArrayAdapter<String> mArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);
        mStringList = new ArrayList<>();
        mContentValues = new ArrayList<>();
        bindID(view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utility.addSelectCounty(mContentValues.get(i).get("city").toString(), mContentValues
                        .get(i).get("id").toString(), "-", "--℃");
                MainActivity activity = (MainActivity) getActivity();
                activity.getFragment(1);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStringList != null && mArrayAdapter != null) {
            mStringList.clear();
            mArrayAdapter.notifyDataSetChanged();
        }
    }

    private void bindID(View view) {
        mBackIV = view.findViewById(R.id.fm_search_iv_back);
        mBackIV.setOnClickListener(this);
        mEditText = view.findViewById(R.id.fm_search_et_city_name);
        mSearchIV = view.findViewById(R.id.fm_search_iv_search);
        mSearchIV.setOnClickListener(this);
        mListView = view.findViewById(R.id.fm_search_lv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fm_search_iv_back:
                MainActivity activity = (MainActivity) getActivity();
                activity.getFragment(1);
                break;
            case R.id.fm_search_iv_search:
                String temp = mEditText.getText().toString();
                if (temp.equals(""))
                    Toast.makeText(getContext(), "请输入城市名称", Toast.LENGTH_SHORT).show();
                else {
                    String searchCity = HttpUrl.CHINA_NAME_SEARCH_START + temp + HttpUrl.HE_WEATHER_KEY;
                    HttpUtil.sendOkHttpRequest(searchCity, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            mContentValues = Utility.handleSearchResponse(response.body().string());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mContentValues.size() > 0) {
                                        mStringList.clear();
                                        for (int i = 0; i < mContentValues.size(); i++) {
                                            mStringList.add(mContentValues.get(i).get("city").toString
                                                    ());
                                        }
                                        if (mArrayAdapter == null) {
                                            mArrayAdapter = new ArrayAdapter<String>(getContext(),
                                                    android.R
                                                            .layout.simple_list_item_1, mStringList);
                                            mListView.setAdapter(mArrayAdapter);
                                        } else {
                                            mArrayAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "没有查找到城市", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                            });
                        }
                    });
                }
                break;
        }
    }
}
