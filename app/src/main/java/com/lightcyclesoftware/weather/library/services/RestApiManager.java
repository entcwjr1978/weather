package com.lightcyclesoftware.weather.library.services;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.Environment;

import com.lightcyclesoftware.weather.library.entities.DailyForecast;
import com.lightcyclesoftware.weather.library.entities.FiveDayDailyForecast;
import com.lightcyclesoftware.weather.library.entities.JsonFlickrApi;
import com.lightcyclesoftware.weather.library.entities.WeatherData;
import com.lightcyclesoftware.weather.library.utils.Utils;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import rx.Observable;

/**
 * Created by ewilliams on 11/7/15.
 */
public class RestApiManager {

    Cache cache;
    public static final int SIZE_OF_CACHE = 2 * 1024 * 1024;
    public static RestApiManager getInstance() {
        return new RestApiManager();
    }

    public RestApiManager() {
        // Create Cache
        cache = null;
    }

    public Observable<JsonFlickrApi> getFlickrImages(String url, Context context) {
        URI uri = URI.create(url);

        cache = new Cache(new File(context.getCacheDir(), "http"), SIZE_OF_CACHE);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setCache(cache);
        Client client = new OkClient(httpClient);
        httpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://" + uri.getHost() + uri.getPath())
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return restAdapter.create(WeatherRestApi.class).getFlickrImages(Utils.buildQueryMap(uri));
    }

    public Observable<FiveDayDailyForecast> getFiveDayDailyForecast(String url, Context context) {
        URI uri = URI.create(url);
        OkHttpClient httpClient = new OkHttpClient();
        Client client = new OkClient(httpClient);
        httpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://" + uri.getHost() + uri.getPath())
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return restAdapter.create(WeatherRestApi.class).getWeatherData(Utils.buildQueryMap(uri))
                .concatMap(r -> {
                    FiveDayDailyForecast fiveDayDailyForecast = new FiveDayDailyForecast();
                    for (int i = 0; i < 5; i++) {
                        DailyForecast dailyForecast = new DailyForecast();
                        List<WeatherData.WeatherItem> weatherItemList = new ArrayList<WeatherData.WeatherItem>();
                        for (int j=0; j < 8; j++) {
                            weatherItemList.add(r.list.get((8*i) + j));
                        }
                        dailyForecast.setLow(Utils.getLowTemp(weatherItemList));
                        dailyForecast.setHigh(Utils.getHighTemp(weatherItemList));
                        fiveDayDailyForecast.getDailyForecastList().add(dailyForecast);
                    }
                    return Observable.just(fiveDayDailyForecast);
                });
    }

}
