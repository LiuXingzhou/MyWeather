package com.islxz.myweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Qingsu on 2017/6/13.
 */

public class SelectCounty extends DataSupport {

    private int selectCountyId;
    private String countyName;
    private String weatherId;
    private String info;
    private String tmp;

    public int getSelectCountyId() {
        return selectCountyId;
    }

    public void setSelectCountyId(int selectCountyId) {
        this.selectCountyId = selectCountyId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }
}
