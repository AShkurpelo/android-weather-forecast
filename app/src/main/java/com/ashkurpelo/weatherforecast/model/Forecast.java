package com.ashkurpelo.weatherforecast.model;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class Forecast implements Comparable<Forecast> {

    private Calendar calendar;
    private long dateUTC;
    private String dateString;
    private double temp;
    private double temp_min;
    private double temp_max;
    private double pressure;
    private double pressure_sea_level;
    private double pressure_grnd_level;
    private int humidity;

    private int weather_id;
    private String main;
    private String description;
    private String icon;

    private int clouds;
    private double wind_speed;
    private double wind_deg;


    @Override
    public int compareTo(@NonNull Forecast o) {
        return o.calendar.compareTo(o.calendar);
    }

    public long getDateUtc() {
        return dateUTC;
    }

    public String getDateString() {
        return dateString;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public double getTemperature() {
        return temp;
    }

    public double getMinTemperature() {
        return temp_min;
    }

    public double getMaxTemperature() {
        return temp_max;
    }

    public double getPressure() {
        return pressure;
    }

    public double getPressureSeaLevel() {
        return pressure_sea_level;
    }

    public double getPressureGroundLevel() {
        return pressure_grnd_level;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getWeatherId() {
        return weather_id;
    }

    public String getWeatherName() {
        return main;
    }

    public String getWeatherDescription() {
        return description;
    }

    public String getWeatherIcon() {
        return icon;
    }

    public int getClouds() {
        return clouds;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public double getWind_deg() {
        return wind_deg;
    }


    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setDateUTC(long dateUTC) {
        this.dateUTC = dateUTC;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setPressure_sea_level(double pressure_sea_level) {
        this.pressure_sea_level = pressure_sea_level;
    }

    public void setPressure_grnd_level(double pressure_grnd_level) {
        this.pressure_grnd_level = pressure_grnd_level;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setWeather_id(int weather_id) {
        this.weather_id = weather_id;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public void setWind_deg(double wind_deg) {
        this.wind_deg = wind_deg;
    }
}
