package com.ashkurpelo.weatherforecast.model;

public class Coordinates {
    public Coordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "lat=" + lat + "&lon=" + lon;
    }

    public double lat;
    public double lon;
}
