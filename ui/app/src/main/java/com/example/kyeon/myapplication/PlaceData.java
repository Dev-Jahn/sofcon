package com.example.kyeon.myapplication;

public class PlaceData {
    private String type;
    private String name;
    private String lat;
    private String lng;

    protected static final String jsonType = "class";
    protected static final String jsonName = "name_kor";
    protected static final String jsonLat = "latitude";
    protected static final String jsonLng = "longitude";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
