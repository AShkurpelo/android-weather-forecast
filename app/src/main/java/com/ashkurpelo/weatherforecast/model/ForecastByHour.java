package com.ashkurpelo.weatherforecast.model;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

//Forecast for each hour value
public class ForecastByHour extends TreeMap<Integer, Forecast> {
    private List<Double> temperatures;
    private List<Double> humidity;
    private double minTemp = Double.MAX_VALUE;
    private double maxTemp = Double.MIN_VALUE;

    public double getMinTemp() {
        if(temperatures == null) extractValues();
        return minTemp;
    }

    public double getMaxTemp() {
        if(temperatures == null) extractValues();
        return maxTemp;
    }

    public Forecast tryGet(int key){
        if(containsKey(key)){
            return get(key);
        }
        else{
            return get(keySet().toArray()[key/3]);
        }
    }
    public Forecast getMiddayForecast(){
        if(containsKey(12))
            return get(12);

        return get(firstKey());
    }
    public List<Forecast> asList(){
        List<Forecast> res = new ArrayList<>();
        for(Integer key : keySet()){
            res.add(get(key));
        }
        return res;
    }

    public List<Double> extractTemperature() {
        if(temperatures == null) extractValues();
        return temperatures;
    }

    public  List<Double> extractHumidity(){
        if(humidity == null) extractValues();
        return humidity;
    }

    private void extractValues() {
        ArrayList<Double> temp = new ArrayList<>();
        ArrayList<Double> hum = new ArrayList<>();
        for (int k : keySet()) {
            Forecast item = get(k);
            double t = item.getTemperature();
            double h = item.getHumidity();
            if(t<minTemp) minTemp = t;
            if(t>maxTemp) maxTemp = t;
            temp.add(t);
            hum.add((h));
        }
        temperatures = temp;
        humidity = hum;
    }

}
