package com.ashkurpelo.weatherforecast.model;


import android.content.Intent;

import com.ashkurpelo.weatherforecast.service.GeoService;

import java.util.Calendar;

public class GeoData {
    public boolean success = false;
    public String locationNameLocalized;
    public String locationName;
    public Calendar lastUpdate = Calendar.getInstance();
    public GeoData(boolean success, String locationName, String locationNameLocalized){
        this.success = success;
        this.locationName = locationName;
        this.locationNameLocalized = locationNameLocalized;
    }
    public GeoData(Intent intent){
        try {
            success = intent.getBooleanExtra(GeoService.EXTRA_RESULTOK_OUT, false);
            if (success) {
                locationName = intent.getStringExtra(GeoService.EXTRA_CITY_OUT);
                locationNameLocalized = intent.getStringExtra(GeoService.EXTRA_CITY_LOCALIZED_OUT);
            }
        } catch (Exception e){
            e.fillInStackTrace();
        }
    }
}
