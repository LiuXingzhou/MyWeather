package com.islxz.myweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.islxz.myweather.MainActivity;
import com.islxz.myweather.R;
import com.islxz.myweather.db.SelectCounty;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qingsu on 2017/6/13.
 */

public class AllWeatherFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ImageView mBackImageView;

    private List<View> mViewList;
    private List<SelectCounty> mSelectCountyList;
    private MyRecyclerViewAdapter mMyRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_weather, null);
        bindID(view);
        mViewList = new ArrayList<>();
        initDate(inflater);
        mRecyclerView.setAdapter(mMyRecyclerViewAdapter);
        return view;
    }

    private void initDate(LayoutInflater inflater) {
        mSelectCountyList = DataSupport.findAll(SelectCounty.class);
        if (mSelectCountyList.size() > 0) {
            mViewList.clear();
            for (SelectCounty selectCounty : mSelectCountyList) {
                View v = inflater.inflate(R.layout.aw_item, null);
                TextView textView = v.findViewById(R.id.item_aw_tv_county_name);
                textView.setText(selectCounty.getCountyName() + "市");
                TextView textView1 = v.findViewById(R.id.item_aw_tv_info);
                textView1.setText(selectCounty.getInfo());
                TextView textView2 = v.findViewById(R.id.item_aw_tv_tmp);
                textView2.setText(selectCounty.getTmp());
                mViewList.add(v);
            }
        }
    }

    private void bindID(View view) {
        mBackImageView = view.findViewById(R.id.fm_aw_iv_back);
        mBackImageView.setOnClickListener(this);
        mRecyclerView = view.findViewById(R.id.fm_aw_rv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fm_aw_iv_back:
                MainActivity activity = (MainActivity) getActivity();
                activity.getFragment(0);
                break;
        }
    }

    /**
     * RecyclerView适配器
     */
    class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView
            .ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mViewList.size();
        }
    }
}
