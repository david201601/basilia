package com.example.davidfoo.basilia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author david.foo
 */
public class WeatherEntity {
        public  String Base;

    @Expose
    @SerializedName("weather")
    public List<Weather> Weather;


        public  class  Weather {
            @SerializedName("id")
            public  int  id;
            @SerializedName("main")
            public  String main;
            @SerializedName("description")
            public  String description;
            @SerializedName("icon")
            public  String icon;
        }

    public List<Weather> getWeather() {
        return Weather;
    }

    public void setWeather(List<Weather> weather) {
        this.Weather = weather;
    }




}
