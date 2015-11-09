package com.lightcyclesoftware.weather.library.entities;

/**
 * Created by ewilliams on 11/9/15.
 */
public class DailyForecast {
    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private double low = 0;
    private double high = 0;
    private String description;
    private int id;
}
