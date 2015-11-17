package com.lightcyclesoftware.weather;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lightcyclesoftware.weather.library.entities.FiveDayDailyForecast;
import com.lightcyclesoftware.weather.library.entities.JsonFlickrApi;
import com.lightcyclesoftware.weather.library.entities.WeatherData;
import com.lightcyclesoftware.weather.library.services.RestApiManager;
import com.lightcyclesoftware.weather.library.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    static final String DEGREE  = "\u00b0";
    static HashMap<Integer, String> imageMap = new HashMap();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageMap.put(200, "tstorms");
        imageMap.put(201, "tstorms");
        imageMap.put(202, "tstorms");
        imageMap.put(210, "tstorms");
        imageMap.put(211, "tstorms");
        imageMap.put(212, "tstorms");
        imageMap.put(221, "tstorms");
        imageMap.put(230, "tstorms");
        imageMap.put(231, "tstorms");
        imageMap.put(232, "tstorms");

        imageMap.put(300, "rain");
        imageMap.put(301, "rain");
        imageMap.put(302, "rain");
        imageMap.put(310, "rain");
        imageMap.put(311, "rain");
        imageMap.put(312, "rain");
        imageMap.put(321, "rain");

        imageMap.put(500, "rain");
        imageMap.put(501, "rain");
        imageMap.put(502, "rain");
        imageMap.put(503, "rain");
        imageMap.put(504, "rain");
        imageMap.put(511, "rain");
        imageMap.put(520, "rain");
        imageMap.put(521, "rain");
        imageMap.put(522, "rain");

        imageMap.put(600, "flurries");
        imageMap.put(601, "snow");
        imageMap.put(602, "snow");
        imageMap.put(611, "sleet");
        imageMap.put(622, "snow");

        imageMap.put(701, "hazy");
        imageMap.put(711, "hazy");
        imageMap.put(721, "hazy");
        imageMap.put(731, "hazy");
        imageMap.put(741, "hazy");

        imageMap.put(800, "clear");
        imageMap.put(801, "mostlycloudy");
        imageMap.put(802, "mostlycloudy");
        imageMap.put(803, "mostlycloudy");
        imageMap.put(804, "cloudy");


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            Button photoButton = (Button) rootView.findViewById(R.id.photoButton);
            init(rootView);
            photoButton.setVisibility(View.VISIBLE);
            photoButton.setBackgroundColor(Color.TRANSPARENT);
            photoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init(rootView);
                }
            });
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        private void init(final View rootView) {
            final ImageView imageView = (ImageView) rootView.findViewById(R.id.cityImageView);
            Observable.zip(
                    Observable.timer(0, 1, TimeUnit.MINUTES),
                    RestApiManager.getInstance().getFlickrImages("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=5d4ac22d0bad43e97252fe823954655d&tag_mode=all&media=photos&tags=atlanta,skyline,city&format=json&extras=url_h&nojsoncallback=1&per_page=250", getActivity())
                    .repeat()
                    , (a, b) -> {
                        return b;
                    })
                    .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<JsonFlickrApi>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            System.out.println(e.getMessage());
                        }

                        @Override
                        public void onNext(JsonFlickrApi jsonFlickrApi) {

                            for (Iterator<JsonFlickrApi.Photo> iter = jsonFlickrApi.photos.photo.listIterator(); iter.hasNext(); ) {
                                JsonFlickrApi.Photo photo = iter.next();
                                if (photo.url_h == null) {
                                    iter.remove();
                                }
                            }

                            String imageUrl = jsonFlickrApi.photos.photo.get(Utils.randInt(0, jsonFlickrApi.photos.photo.size() - 1)).url_h;
                            Picasso.with(getActivity())
                                    .load(imageUrl)
                                    .fit()
                                    .placeholder(imageView.getDrawable())
                                    .centerCrop().into(imageView, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    RestApiManager.getInstance().getFiveDayDailyForecast("http://api.openweathermap.org/data/2.5/forecast/daily?q=Atlanta,us&cnt=5&mode=json&APPID=1be08fee7477f0a22dbea39cc1a1cbd7&units=imperial", getActivity())
                                            .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Subscriber<WeatherData>() {

                                                @Override
                                                public void onCompleted() {

                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    System.out.println(e.getMessage());
                                                }

                                                @Override
                                                public void onNext(WeatherData weatherData) {
                                                    CardView day0 = (CardView) rootView.findViewById(R.id.day0);
                                                    CardView day1 = (CardView) rootView.findViewById(R.id.day1);
                                                    CardView day2 = (CardView) rootView.findViewById(R.id.day2);
                                                    CardView day3 = (CardView) rootView.findViewById(R.id.day3);
                                                    CardView day4 = (CardView) rootView.findViewById(R.id.day4);


                                                    Palette palette = Palette.generate(((BitmapDrawable) imageView.getDrawable()).getBitmap());
                                                    int vibrant = palette.getVibrantColor((225 << 24) | (0x000000 & 0x00ffffff));
                                                    int vibrantLight = palette.getLightVibrantColor((225 << 24) | (0x000000 & 0x00ffffff));
                                                    int vibrantDark = palette.getDarkVibrantColor((225 << 24) | (0x000000 & 0x00ffffff));
                                                    int muted = palette.getMutedColor((225 << 24) | (0x000000 & 0x00ffffff));
                                                    int mutedLight = palette.getLightMutedColor((225 << 24) | (0x000000 & 0x00ffffff));
                                                    int mutedDark = palette.getDarkMutedColor((225 << 24) | (0x000000 & 0x00ffffff));

                                                    Palette.Swatch swatch = palette.getMutedSwatch();
                                                    int titleTextColor = swatch.getTitleTextColor();
                                                    int bodyTextColor = swatch.getBodyTextColor();

                                                    //((TextView) rootView.findViewById(R.id.cityTextView)).setTextColor(titleTextColor);

                                                    Calendar calendar0 = Calendar.getInstance();
                                                    calendar0.setTimeInMillis(Long.parseLong(weatherData.list.get(0).dt) * 1000);
                                                    day0.setCardBackgroundColor((225 << 24) | (muted & 0x00ffffff));
                                                    ((TextView) day0.findViewById(R.id.dayTextView)).setText(calendar0.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
                                                    ((TextView) day0.findViewById(R.id.lowTextView)).setText("Low " + Integer.toString((int) weatherData.list.get(0).temp.min) + DEGREE);
                                                    ((TextView) day0.findViewById(R.id.highTextView)).setText("High " + Integer.toString((int) weatherData.list.get(0).temp.max) + DEGREE);
                                                    ((TextView) day0.findViewById(R.id.dayTextView)).setTextColor(titleTextColor);
                                                    ((TextView) day0.findViewById(R.id.conditionsTextView)).setText(weatherData.list.get(0).weather.get(0).main);
                                                    ((TextView) day0.findViewById(R.id.lowTextView)).setTextColor(bodyTextColor);
                                                    ((TextView) day0.findViewById(R.id.highTextView)).setTextColor(bodyTextColor);
                                                    ((TextView) day0.findViewById(R.id.conditionsTextView)).setTextColor(bodyTextColor);
                                                    ((ImageView) day0.findViewById(R.id.conditionsImageView)).setImageResource(getResources().getIdentifier(imageMap.get(weatherData.list.get(0).weather.get(0).id), "drawable", "com.lightcyclesoftware.weather"));


                                                    Calendar calendar1 = Calendar.getInstance();
                                                    calendar1.setTimeInMillis(Long.parseLong(weatherData.list.get(1).dt) * 1000);
                                                    day1.setCardBackgroundColor((225 << 24) | (muted & 0x00ffffff));


                                                    ((TextView) day1.findViewById(R.id.dayTextView)).setText(calendar1.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
                                                    ((TextView) day1.findViewById(R.id.lowTextView)).setText("Low " + Integer.toString((int) weatherData.list.get(1).temp.min) + DEGREE);
                                                    ((TextView) day1.findViewById(R.id.highTextView)).setText("High " + Integer.toString((int) weatherData.list.get(1).temp.max) + DEGREE);
                                                    ((TextView) day1.findViewById(R.id.conditionsTextView)).setText(weatherData.list.get(1).weather.get(0).main);
                                                    ((TextView) day1.findViewById(R.id.dayTextView)).setTextColor(titleTextColor);
                                                    ((TextView) day1.findViewById(R.id.lowTextView)).setTextColor(bodyTextColor);
                                                    ((TextView) day1.findViewById(R.id.highTextView)).setTextColor(bodyTextColor);
                                                    ((TextView) day1.findViewById(R.id.conditionsTextView)).setTextColor(bodyTextColor);
                                                    ((ImageView) day1.findViewById(R.id.conditionsImageView)).setImageResource(getResources().getIdentifier(imageMap.get(weatherData.list.get(1).weather.get(0).id), "drawable", "com.lightcyclesoftware.weather"));


                                                    Calendar calendar2 = Calendar.getInstance();
                                                    calendar2.setTimeInMillis(Long.parseLong(weatherData.list.get(2).dt) * 1000);
                                                    day2.setCardBackgroundColor((225 << 24) | (muted & 0x00ffffff));


                                                    ((TextView) day2.findViewById(R.id.dayTextView)).setText(calendar2.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
                                                    ((TextView) day2.findViewById(R.id.lowTextView)).setText("Low " + Integer.toString((int) weatherData.list.get(2).temp.min) + DEGREE);
                                                    ((TextView) day2.findViewById(R.id.highTextView)).setText("High " + Integer.toString((int) weatherData.list.get(2).temp.max) + DEGREE);
                                                    ((TextView) day2.findViewById(R.id.dayTextView)).setTextColor(titleTextColor);
                                                    ((TextView) day2.findViewById(R.id.conditionsTextView)).setText(weatherData.list.get(2).weather.get(0).main);
                                                    ((TextView) day2.findViewById(R.id.lowTextView)).setTextColor(bodyTextColor);
                                                    ((TextView) day2.findViewById(R.id.highTextView)).setTextColor(bodyTextColor);
                                                    ((TextView) day2.findViewById(R.id.conditionsTextView)).setTextColor(bodyTextColor);
                                                    ((ImageView) day2.findViewById(R.id.conditionsImageView)).setImageResource(getResources().getIdentifier(imageMap.get(weatherData.list.get(2).weather.get(0).id), "drawable", "com.lightcyclesoftware.weather"));

                                                    Calendar calendar3 = Calendar.getInstance();
                                                    calendar3.setTimeInMillis(Long.parseLong(weatherData.list.get(3).dt) * 1000);
                                                    day3.setCardBackgroundColor((225 << 24) | (muted & 0x00ffffff));


                                                    ((TextView) day3.findViewById(R.id.dayTextView)).setText(calendar3.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
                                                    ((TextView) day3.findViewById(R.id.lowTextView)).setText("Low " + Integer.toString((int) weatherData.list.get(3).temp.min) + DEGREE);
                                                    ((TextView) day3.findViewById(R.id.highTextView)).setText("High " + Integer.toString((int) weatherData.list.get(3).temp.max) + DEGREE);
                                                    ((TextView) day3.findViewById(R.id.dayTextView)).setTextColor(titleTextColor);
                                                    ((TextView) day3.findViewById(R.id.conditionsTextView)).setText(weatherData.list.get(3).weather.get(0).main);
                                                    ((TextView) day3.findViewById(R.id.lowTextView)).setTextColor(bodyTextColor);
                                                    ((TextView) day3.findViewById(R.id.highTextView)).setTextColor(bodyTextColor);
                                                    ((TextView) day3.findViewById(R.id.conditionsTextView)).setTextColor(bodyTextColor);
                                                    ((ImageView) day3.findViewById(R.id.conditionsImageView)).setImageResource(getResources().getIdentifier(imageMap.get(weatherData.list.get(3).weather.get(0).id), "drawable", "com.lightcyclesoftware.weather"));

                                                    Calendar calendar4 = Calendar.getInstance();
                                                    calendar4.setTimeInMillis(Long.parseLong(weatherData.list.get(4).dt) * 1000);
                                                    day4.setCardBackgroundColor((225 << 24) | (muted & 0x00ffffff));

                                                    ((TextView) day4.findViewById(R.id.dayTextView)).setText(calendar4.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
                                                    ((TextView) day4.findViewById(R.id.lowTextView)).setText("Low " + Integer.toString((int) weatherData.list.get(4).temp.min) + DEGREE);
                                                    ((TextView) day4.findViewById(R.id.highTextView)).setText("High " + Integer.toString((int) weatherData.list.get(4).temp.max) + DEGREE);
                                                    ((TextView) day4.findViewById(R.id.dayTextView)).setTextColor(titleTextColor);
                                                    ((TextView) day4.findViewById(R.id.conditionsTextView)).setText(weatherData.list.get(4).weather.get(0).main);
                                                    ((TextView) day4.findViewById(R.id.lowTextView)).setTextColor(bodyTextColor);
                                                    ((TextView) day4.findViewById(R.id.highTextView)).setTextColor(bodyTextColor);
                                                    ((TextView) day4.findViewById(R.id.conditionsTextView)).setTextColor(bodyTextColor);
                                                    ((ImageView) day4.findViewById(R.id.conditionsImageView)).setImageResource(getResources().getIdentifier(imageMap.get(weatherData.list.get(4).weather.get(0).id), "drawable", "com.lightcyclesoftware.weather"));

                                                }
                                            });
                                }

                                @Override
                                public void onError() {
                                    System.out.println("error!");
                                }
                            });
                        }
                    });
            }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }


}
