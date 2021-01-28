package com.ashkurpelo.weatherforecast.model;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Map of ForecastByHour for each day
 */
public class DailyForecastMap extends TreeMap<Integer, ForecastByHour> {
    public List<ForecastByHour> asList(){
        List<ForecastByHour> res = new ArrayList<>();
        for(int k : keySet()){
            res.add(get(k));
        }
        return res;
    }
}
