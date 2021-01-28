package com.ashkurpelo.weatherforecast.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * A singleton class
 */

public class PreferenceManager {
    private static PreferenceManager instance;
    private static String PREFS_NAME = "MAIN_PREFERENCES";
    static final String PREF_LOCATION_KEY = "LocationCity";
    static final String PREF_LOCATION_LOCALIZED_KEY = "LocalizedCity";
    static final String PREF_LAST_UPDATE = "LastUpdate";
    private SharedPreferences prefs;


    private PreferenceManager(SharedPreferences preferences) {
        this.prefs = preferences;
    }

    public static synchronized PreferenceManager getInstance(Context ctx) {
        if (instance == null) {
            instance = new PreferenceManager(ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE));
        }
        return instance;
    }

    public static synchronized PreferenceManager getInstance() {
        if (instance == null) {
            throw new NullPointerException("Singleton class not initialized");
        }
        return instance;
    }


    public long getLastUpdated() {
        return prefs.getLong(PREF_LAST_UPDATE, Calendar.getInstance().getTimeInMillis());
    }

    public void setLastUpdated(long time) {
        prefs.edit()
                .putLong(PREF_LAST_UPDATE, time)
                .apply();
    }

    public String getLocationName() {
        return prefs.getString(PREF_LOCATION_KEY, null);
    }

    public void setLocationName(String name){
        prefs.edit()
                .putString(PREF_LOCATION_KEY, name)
                .apply();
    }

    public String getLocationNameLocalized(){
        return prefs.getString(PREF_LOCATION_LOCALIZED_KEY, null);
    }

    public void setLocationNameLocalized(String localizedName) {
        prefs.edit()
                .putString(PREF_LOCATION_LOCALIZED_KEY, localizedName)
                .apply();
    }


}
