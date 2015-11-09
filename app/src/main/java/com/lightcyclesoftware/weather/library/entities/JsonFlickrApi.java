package com.lightcyclesoftware.weather.library.entities;

import java.util.List;

/**
 * Created by ewilliams on 11/7/15.
 */
public class JsonFlickrApi {
    public Photos photos;

    public class Photos {
        public List<Photo> photo;
    }

    public class Photo {
        public String id;
        public String url_h;
    }
}
