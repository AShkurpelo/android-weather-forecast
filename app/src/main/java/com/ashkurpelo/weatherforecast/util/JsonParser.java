package com.ashkurpelo.weatherforecast.util;


import com.ashkurpelo.weatherforecast.model.City;
import com.ashkurpelo.weatherforecast.model.DailyForecastMap;
import com.ashkurpelo.weatherforecast.model.Forecast;
import com.ashkurpelo.weatherforecast.model.ForecastByHour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class JsonParser {


    private DailyForecastMap forecastByDay;
    private JSONObject json;

    private String jsonString;


    private boolean valid = false;

    private static Calendar now = Calendar.getInstance();
    private City city;

    public JsonParser(String jsonString) {
        try {
            this.jsonString = jsonString;
            json = new JSONObject(jsonString);
            city = new City(json.getJSONObject("city"));
            int cnt = json.getInt("cnt");
            JSONArray arr = json.getJSONArray("list");

            forecastByDay = new DailyForecastMap();
            int minDay = 366;
            for (int i = 0; i < cnt; ++i) {
                JSONObject obj = arr.getJSONObject(i);
                Forecast forecast = new Forecast();
                forecast.setDateUTC(obj.getLong("dt"));
                forecast.setDateString(obj.getString("dt_txt"));
                //forecast.setDate(new Date(forecast.dateString));
                forecast.setCalendar(Calendar.getInstance());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    forecast.getCalendar().setTime(sdf.parse(forecast.getDateString()));
                } catch (ParseException pe) {
                    pe.fillInStackTrace();
                }
                //If older than 3 hours
                if (forecast.getCalendar().getTimeInMillis() - now.getTimeInMillis() < -1000 * 60 * 60 * 3) {
                    continue;
                }

                JSONObject main = obj.getJSONObject("main");
                forecast.setTemp(main.getDouble("temp"));
                forecast.setTemp_min(main.getDouble("temp_min"));
                forecast.setTemp_max(main.getDouble("temp_max"));
                forecast.setPressure(main.getDouble("pressure"));
                forecast.setPressure_sea_level(main.getDouble("sea_level"));
                forecast.setPressure_grnd_level(main.getDouble("grnd_level"));
                forecast.setHumidity(main.getInt("humidity"));

                // TODO: support for several weather conditions (https://openweathermap.org/weather-conditions) => parse if misty

                JSONObject weatherObj = obj.getJSONArray("weather").getJSONObject(0);
                forecast.setWeather_id(weatherObj.getInt("id"));
                forecast.setMain(weatherObj.getString("main"));
                forecast.setDescription(weatherObj.getString("description"));
                forecast.setIcon(weatherObj.getString("icon"));

                forecast.setClouds(obj.getJSONObject("clouds").getInt("all"));
                forecast.setWind_deg(obj.getJSONObject("wind").getDouble("deg"));
                forecast.setWind_speed(obj.getJSONObject("wind").getDouble("speed"));


                int day = forecast.getCalendar().get(Calendar.DAY_OF_YEAR);
                if (day < minDay) minDay = day;
                int hour = forecast.getCalendar().get(Calendar.HOUR_OF_DAY);
                insertForecast(forecast, day - minDay, hour);
            }
            valid = true;
        } catch (JSONException e) {
            e.printStackTrace();
            valid = false;
        }
    }

    private void insertForecast(Forecast forecast, int day, int hour) {
        ForecastByHour fbh = forecastByDay.get(day);

        if (fbh == null) {
            fbh = new ForecastByHour();
            fbh.put(hour, forecast);
            forecastByDay.put(day, fbh);
        } else {
            fbh.put(hour, forecast);
            forecastByDay.put(day, fbh);
        }
    }

    public DailyForecastMap getForecastByDay() {
        return forecastByDay;
    }

    public boolean isValid() {
        return valid;
    }

    public City getCity() {
        return city;
    }

    public String getJsonString() {
        return jsonString;
    }

}
