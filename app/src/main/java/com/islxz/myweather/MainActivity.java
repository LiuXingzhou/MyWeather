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
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.islxz.myweather.fragment.AllWeatherFragment;
import com.islxz.myweather.fragment.SearchFragment;
import com.islxz.myweather.fragment.WeatherFragment;
import com.islxz.myweather.util.HttpUrl;
import com.islxz.myweather.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

    private List<String> mSelectCityList;

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

        ifFirstRun();

        String bingPic = mSharedPreferences.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(mBingPicIV);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCityData();
        mFragmentList = new ArrayList<Fragment>();
        for (int i = 0; i < mSelectCityList.size(); i++) {
            WeatherFragment mWeatherFragment = new WeatherFragment();
            mWeatherFragment.initData(mSelectCityList.get(i));
            mFragmentList.add(mWeatherFragment);
        }
        mFragmentManager = getSupportFragmentManager();
        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(mFragmentManager);
        mViewPager.setAdapter(mMyFragmentPagerAdapter);
        showThis("vp");
    }

    private void initCityData() {
        mSelectCityList = new ArrayList<>();
        mSelectCityList.add("CN101100202");
        mSelectCityList.add("CN101100206");
        mSelectCityList.add("CN101100207");
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

    /**
     * 监听事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_fab:
                showThis("fl");
                mFragmentTransaction = mFragmentManager.beginTransaction();
                if (mAllWeatherFragment == null) {
                    mAllWeatherFragment = new AllWeatherFragment();
                }
                mFragmentTransaction.replace(R.id.main_fl, mAllWeatherFragment);
                mFragmentTransaction.commit();
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
