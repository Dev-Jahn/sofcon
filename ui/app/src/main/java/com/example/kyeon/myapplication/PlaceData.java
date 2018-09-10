package com.example.kyeon.myapplication;

import com.google.android.gms.maps.model.LatLng;

public class PlaceData {
    private String placeId;
    private String type;
    private String name;
    private String lat;
    private String lng;

    protected static final String jsonPlaceID = "placeId";
    protected static final String jsonType = "class";
    protected static final String jsonName = "name_kor";
    protected static final String jsonLat = "latitude";
    protected static final String jsonLng = "longitude";

    public PlaceData() {

    }

    public PlaceData(String title, String snippet, LatLng latLng) {
        this.name = title;
        this.type = snippet;
        this.lat = String.valueOf(latLng.latitude);
        this.lng = String.valueOf(latLng.longitude);
    }

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

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
