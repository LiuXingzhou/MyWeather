package com.islxz.myweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.islxz.myweather.db.SelectCounty;
import com.islxz.myweather.fragment.AllWeatherFragment;
import com.islxz.myweather.fragment.SearchFragment;
import com.islxz.myweather.fragment.WeatherFragment;
import com.islxz.myweather.util.HttpUrl;
import com.islxz.myweather.util.HttpUtil;
import com.islxz.myweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int LEVEL_WEATHER = 0;
    private static final int LEVEL_ALL_WEATHER = 1;
    private static final int LEVEL_SEARCH = 2;

    private int curreentLevel;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private SearchFragment mSearchFragment;
    private AllWeatherFragment mAllWeatherFragment;
    private FrameLayout mFrameLayout;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private FloatingActionButton mFloatingActionButton;

    private ViewPager mViewPager;
    private List<Fragment> mFragmentList;
    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;

    private ImageView mBingPicIV;

    private List<SelectCounty> mSelectCityList;
    private List<String> mWeatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 浸润式状态栏
         */
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams
                .FLAG_TRANSLUCENT_STATUS);
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager
                .LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        bindID();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        mWeatherId = new ArrayList<String>();
        mFragmentList = new ArrayList<>();

        ifFirstRun();

        String bingPic = mSharedPreferences.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(mBingPicIV);
        }
        getFragment(LEVEL_WEATHER);
    }

    /**
     * 绑定ID
     */
    private void bindID() {
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.main_fab);
        mFloatingActionButton.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.mian_vp);
        mFrameLayout = (FrameLayout) findViewById(R.id.main_fl);
        mBingPicIV = (ImageView) findViewById(R.id.main_iv);
    }

    private void ifFirstRun() {
        boolean isFirstRun = mSharedPreferences.getBoolean("isfirstrun", true);
        if (isFirstRun) {
            loadBingPic();
            mEditor.putBoolean("isfirstrun", false);
            mEditor.apply();
        } else return;
    }

    public void getFragment(int arg0) {
        switch (arg0) {
            case LEVEL_WEATHER:
                curreentLevel = LEVEL_WEATHER;
                mFloatingActionButton.setVisibility(View.VISIBLE);
                mFloatingActionButton.setImageResource(R.drawable.ic_list);
                showThis("vp");
                initCityData();
                mFragmentList.clear();
                for (int i = 0; i < mWeatherId.size(); i++) {
                    WeatherFragment mWeatherFragment = new WeatherFragment();
                    mWeatherFragment.initData(mWeatherId.get(i));
                    mFragmentList.add(mWeatherFragment);
                }
                mFragmentManager = getSupportFragmentManager();
                if (mMyFragmentPagerAdapter == null) {
                    mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(mFragmentManager);
                    mViewPager.setAdapter(mMyFragmentPagerAdapter);
                } else {
                    mMyFragmentPagerAdapter.notifyDataSetChanged();
                }
                break;
            case LEVEL_ALL_WEATHER:
                curreentLevel = LEVEL_ALL_WEATHER;
                mFloatingActionButton.setVisibility(View.VISIBLE);
                mFloatingActionButton.setImageResource(R.drawable.ic_add);
                showThis("fl");
                mFragmentTransaction = mFragmentManager.beginTransaction();
                if (mAllWeatherFragment == null) {
                    mAllWeatherFragment = new AllWeatherFragment();
                }
                mFragmentTransaction.replace(R.id.main_fl, mAllWeatherFragment);
                mFragmentTransaction.commit();
                break;
            case LEVEL_SEARCH:
                curreentLevel = LEVEL_SEARCH;
                mFloatingActionButton.setVisibility(View.INVISIBLE);
                mFragmentTransaction = mFragmentManager.beginTransaction();
                if (mSearchFragment == null) {
                    mSearchFragment = new SearchFragment();
                }
                mFragmentTransaction.replace(R.id.main_fl, mSearchFragment);
                mFragmentTransaction.commit();
                break;
        }
    }

    private void initCityData() {
        Utility.addSelectCounty("阳高", "CN101100202", "晴", "18℃");
        mSelectCityList = DataSupport.findAll(SelectCounty.class);
        if (mSelectCityList.size() > 0) {
            mWeatherId.clear();
            for (SelectCounty selectCounty3 : mSelectCityList) {
                mWeatherId.add(selectCounty3.getWeatherId());
            }
        }
    }

    /**
     * 监听事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_fab:
                if (curreentLevel == LEVEL_WEATHER) {
                    getFragment(LEVEL_ALL_WEATHER);
                } else if (curreentLevel == LEVEL_ALL_WEATHER) {
                    getFragment(LEVEL_SEARCH);
                }
                break;
            case R.id.fm_aw_iv_back:
                getFragment(LEVEL_WEATHER);
                break;
        }
    }

    /**
     * ViewPager，FrameLayout切换显示
     */
    private void showThis(String arg0) {
        switch (arg0) {
            case "vp":
                mFrameLayout.setVisibility(View.INVISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
                break;
            case "fl":
                mViewPager.setVisibility(View.INVISIBLE);
                mFrameLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * FramgentPager适配器
     */
    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(bingPic).into(mBingPicIV);
                    }
                });
            }
        });
    }

}
