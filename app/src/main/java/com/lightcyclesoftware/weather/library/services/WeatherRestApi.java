package com.lightcyclesoftware.weather.library.services;

import com.lightcyclesoftware.weather.library.entities.JsonFlickrApi;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * Created by ewilliams on 11/7/15.
 */
public interface WeatherRestApi {
    @GET("/")
    public Observable<JsonFlickrApi> getFlickrImages(@QueryMap(encodeValues = true) Map<String, String> parameters);
}
