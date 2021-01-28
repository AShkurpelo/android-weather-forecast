package com.ashkurpelo.weatherforecast.model;

import org.json.JSONException;
import org.json.JSONObject;


public class City {
    private String name;
    private int id;
    private Coordinates coord;
    private String country;

    public City() {}

    public City(JSONObject cityNode) throws JSONException {
        setName(cityNode.getString("name"));
        setId(cityNode.getInt("id"));
        setCountry(cityNode.getString("country"));
        JSONObject c = cityNode.getJSONObject("coord");
        setCoord(new Coordinates(c.getDouble("lat"), c.getDouble("lon")));
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(City.class) && getName().equals(((City) obj).getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coordinates getCoord() {
        return coord;
    }

    public void setCoord(Coordinates coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
