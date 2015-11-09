package com.lightcyclesoftware.weather.library.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ewilliams on 11/9/15.
 */
public class FiveDayDailyForecast {

    public FiveDayDailyForecast(){
        dailyForecastList = new ArrayList();
    }

    public List<DailyForecast> getDailyForecastList() {
        return dailyForecastList;
    }

    public void setDailyForecastList(List<DailyForecast> dailyForecastList) {
        this.dailyForecastList = dailyForecastList;
    }

    private List<DailyForecast> dailyForecastList;
}
