package com.lightcyclesoftware.weather.library.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ewilliams on 11/9/15.
 */
public class WeatherData {
    public int cod;
    public String message;
    public City city;
    public int cnt;
    public List<WeatherItem> list;

    public class City {
        public String id;
        public String name;
        public Coordinate coord;
        public String country;
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
        public Temperature temp;
        public double pressure;
        public int humidity;
        public List<Weather> weather;
    }

    public class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;
    }

    public class Temperature {
        public double day;
        public double min;
        public double max;
        public double night;
        public double eve;
        public double morn;
    }
}
