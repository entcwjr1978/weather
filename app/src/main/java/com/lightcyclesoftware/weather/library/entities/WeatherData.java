package com.lightcyclesoftware.weather.library.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ewilliams on 11/9/15.
 */
public class WeatherData {
    public City city;
    public int cod;
    public String message;
    public int cnt;
    public List<WeatherItem> list;
    public class City {
        public String id;
        public String name;
        public Coordinate coord;
        public String country;
        public int population;
        public Sys sys;
    }

    public class Coordinate {
        public double lon;
        public double lat;
    }

    public class Sys {
        public int population;
    }

    public class WeatherItem {
        public String dt;
        public WeatherItemMain main;
        public List<Weather> weather;
        public Clouds clouds;
        public Wind wind;
        public WeatherItemSys sys;
        public String dt_txt;

    }

    public class WeatherItemMain {
        public double temp;
        public double temp_min;
        public double temp_max;
        public double pressure;
        public double sea_level;
        public double grnd_level;
        public double humidity;
        public double temp_kf;
    }

    public class Weather {
        public String id;
        public String main;
        public String description;
        public String icon;
    }

    public class Clouds {
        public int all;
    }

    public class Wind {
        public double speed;
        public double deg;
    }

    public class Rain {
        @SerializedName("3h")
        public double threeH;
    }

    public class WeatherItemSys {
        public String pod;
    }
}
