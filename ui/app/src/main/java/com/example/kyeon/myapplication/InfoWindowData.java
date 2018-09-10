package com.example.kyeon.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowData {
    private String title;
    private String snippet;
    // deprecated
    private String score;
    private int order;
    private String placeID;
    private LatLng latLng;
    private int windowType;

    public static final int TYPE_USER = 0x1234;
    public static final int TYPE_PLACE = 0x4321;
    public static final int TYPE_FIRST_PLACE = 0x1111;

    public InfoWindowData() {

    }

    public InfoWindowData(Marker marker) {
        title = ((InfoWindowData)marker.getTag()).title;
        snippet = ((InfoWindowData)marker.getTag()).snippet;
        score = ((InfoWindowData)marker.getTag()).score;
        order = ((InfoWindowData)marker.getTag()).order;
        placeID = ((InfoWindowData)marker.getTag()).placeID;
        latLng = ((InfoWindowData)marker.getTag()).latLng;
        windowType = ((InfoWindowData)marker.getTag()).windowType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getWindowType() {
        return windowType;
    }

    public void setWindowType(int windowType) {
        this.windowType = windowType;
    }

    @Override
    public String toString() {
        String string = title + "," + snippet + "," + score + "," + order + "," + placeID;
        return string;
    }
}