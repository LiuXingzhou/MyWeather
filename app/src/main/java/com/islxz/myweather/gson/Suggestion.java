package com.islxz.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Qingsu on 2017/6/12.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort{

        @SerializedName("brf")
        public String brfTxt;

        @SerializedName("txt")
        public String info;
    }

    public class CarWash{

        @SerializedName("brf")
        public String brfTxt;

        @SerializedName("txt")
        public String info;
    }

    public class Sport{

        @SerializedName("brf")
        public String brfTxt;

        @SerializedName("txt")
        public String info;
    }
}
