package com.ashkurpelo.weatherforecast.service;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;


public class WeatherPullService extends IntentService {
    private String openweather_appid;
    private final static String TAG = "WeatherPullService";
    public WeatherPullService() {
        super("myname");
    }

    public static final String ACTION_RESULT = "com.ashkurpelo.weaterforecast.ACTION_RESULT";
    public static final String EXTRA_RESULT_JSON_OUT = "com.ashkurpelo.weaterforecast.EXTRA_RESULT_JSON_OUT";
    public static final String EXTRA_RESULT_IS_ERROR = "com.ashkurpelo.weaterforecast.EXTRA_RESULT_IS_ERROR";

    public static final String EXTRA_LOCATIONTYPE = "com.ashkurpelo.weaterforecast.EXTRA_LOCATIONTYPE";
    public static final String EXTRA_LOCATION = "com.ashkurpelo.weaterforecast.EXTRA_LOCATION";
    public static final String EXTRA_APPID = "com.ashkurpelo.weaterforecast.EXTRA_APPID";
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String lang = Locale.getDefault().getISO3Language().substring(0, 2);
        if(lang.equals("uk")) lang = "ru"; //openweathermap doesn't support ukrainian
        apiParams = "units=metric&lang=" + lang;
        LocationType locationType = LocationType.valueOf(intent.getStringExtra(EXTRA_LOCATIONTYPE));
        String location = intent.getStringExtra(EXTRA_LOCATION);
        openweather_appid = intent.getStringExtra(EXTRA_APPID);
        Log.i(TAG, "onHandleIntent: location=" + location);
        String res;
        try{
            URL url;
            switch (locationType){
                case BY_CITY:
                    url = new URL(CreateApiURLByCity(location));
                    break;
                case BY_COORDINATES:
                    url = new URL(CreateApiURLByLocation(location));
                    break;
                default:
                    throw new Exception("locationType not set");
            }

            Log.d(TAG, "Result URL: " + url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                Scanner s = new Scanner(in).useDelimiter("\\A");
                res = s.hasNext() ? s.next() : "error";
            }
            finally{
                conn.disconnect();
            }
        } catch (Exception e){
            Log.e(TAG, "Error getting response: " + e.toString(), e);

            e.fillInStackTrace();
            Intent errorIntent = new Intent()
                    .setAction(ACTION_RESULT)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .putExtra(EXTRA_RESULT_JSON_OUT, ".")
                    .putExtra(EXTRA_RESULT_IS_ERROR, true);
            sendBroadcast(errorIntent);
            return;
        }

        Intent resIntent = new Intent()
                .setAction(ACTION_RESULT)
                .addCategory(Intent.CATEGORY_DEFAULT)
                .putExtra(EXTRA_RESULT_JSON_OUT, res);
                //.putExtra(EXTRA_RESULT_IS_ERROR, false);
        sendBroadcast(resIntent);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(resIntent);
        Log.i(TAG, "sendBroadcast with results");
    }

    private static String apiParams = "units=metric&lang=ru";
    private String CreateApiURLByCity(String city){
        return "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&APPID=" +  openweather_appid + "&" + apiParams;
    }
    private String CreateApiURLByLocation(String location){
        return "http://api.openweathermap.org/data/2.5/forecast?" + location + "&APPID=" + openweather_appid + "&" + apiParams;
    }
    public enum LocationType{
        BY_CITY(0), BY_COORDINATES(1);

        public int getId() {
            return id;
        }

        private final int id;
        LocationType(int id) {
            this.id = id;
        }
    }
}
