package com.islxz.myweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.List;

/**
 * Created by Qingsu on 2017/6/13.
 */

public class AllWeatherFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ImageView mBackImageView;

    private List<SelectCounty> mSelectCountyList;
    private MyRecyclerViewAdapter mMyRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_weather, null);
        bindID(view);
        initDate(inflater);
        mMyRecyclerViewAdapter = new MyRecyclerViewAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mMyRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshDate();
    }

    private void refreshDate() {

    }

    private void initDate(LayoutInflater inflater) {
        mSelectCountyList = DataSupport.findAll(SelectCounty.class);
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
    class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter
            .ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.aw_item, null);
            MyRecyclerViewAdapter.ViewHolder viewHolder = new MyRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.nameTV.setText(mSelectCountyList.get(position).getCountyName());
            holder.infoTV.setText(mSelectCountyList.get(position).getInfo());
            holder.tmpTV.setText(mSelectCountyList.get(position).getTmp() + "℃");
        }

        @Override
        public int getItemCount() {
            return mSelectCountyList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView nameTV;
            TextView infoTV;
            TextView tmpTV;

            public ViewHolder(View itemView) {
                super(itemView);
                nameTV = itemView.findViewById(R.id.item_aw_tv_county_name);
                infoTV = itemView.findViewById(R.id.item_aw_tv_info);
                tmpTV = itemView.findViewById(R.id.item_aw_tv_tmp);
            }
        }
    }
}
