package com.islxz.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Qingsu on 2017/6/12.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{

        @SerializedName("txt")
        public String info;
    }
}
