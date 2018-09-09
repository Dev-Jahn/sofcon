package com.example.kyeon.myapplication;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

public class IntentData {

    private String dYY;
    private String dMM;
    private String dDD;
    private String aYY;
    private String aMM;
    private String aDD;
    private String currentDay;
    private String title;
    private String personCount;
    private String firstPlace;
    private String placeLat;
    private String placeLng;
    private String placeType;
    private String placeBitmapFilePath;
    private LatLng placeLatLng;
    private boolean isLoaded;
    public static final String isLoadedTag = MapUtility.PLACE_LOAD_TAG;

    public IntentData(Intent intent) {
        dYY = intent.getStringExtra(MapUtility.D_YY_TAG);
        dMM = intent.getStringExtra(MapUtility.D_MM_TAG);
        dDD = intent.getStringExtra(MapUtility.D_DD_TAG);
        aYY = intent.getStringExtra(MapUtility.A_YY_TAG);
        aMM = intent.getStringExtra(MapUtility.A_MM_TAG);
        aDD = intent.getStringExtra(MapUtility.A_DD_TAG);
        currentDay = intent.getStringExtra(MapUtility.CURRENT_DAY_TAG);
        title = intent.getStringExtra(MapUtility.TRAVEL_TITLE_TAG);
        personCount = intent.getStringExtra(MapUtility.TRAVEL_PERSON_COUNT_TAG);
        firstPlace = intent.getStringExtra(MapUtility.PLACE_NAME_TAG);
        placeLat = intent.getStringExtra(MapUtility.PLACE_LAT_TAG);
        placeLng = intent.getStringExtra(MapUtility.PLACE_LNG_TAG);
        calcLatLng();
        placeType = intent.getStringExtra(MapUtility.PLACE_TYPE_TAG);
        placeBitmapFilePath = intent.getStringExtra(MapUtility.PLACE_BITMAP_FILE_PATH_TAG);
        isLoaded = intent.getBooleanExtra(MapUtility.PLACE_LOAD_TAG, false);
    }

    protected void transferDataToIntent(Intent intent) {
        intent.putExtra(MapUtility.D_YY_TAG, dYY);
        intent.putExtra(MapUtility.D_MM_TAG, dMM);
        intent.putExtra(MapUtility.D_DD_TAG, dDD);
        intent.putExtra(MapUtility.A_YY_TAG, aYY);
        intent.putExtra(MapUtility.A_MM_TAG, aMM);
        intent.putExtra(MapUtility.A_DD_TAG, aDD);
        intent.putExtra(MapUtility.CURRENT_DAY_TAG, currentDay);
        intent.putExtra(MapUtility.TRAVEL_TITLE_TAG, title);
        intent.putExtra(MapUtility.PLACE_NAME_TAG, firstPlace);
        intent.putExtra(MapUtility.PLACE_LAT_TAG, placeLat);
        intent.putExtra(MapUtility.PLACE_LNG_TAG, placeLng);
        intent.putExtra(MapUtility.PLACE_TYPE_TAG, placeType);
        intent.putExtra(MapUtility.PLACE_BITMAP_FILE_PATH_TAG, placeBitmapFilePath);
    }

    protected void calcLatLng() {
        if(placeLat != null && placeLng != null) {
            placeLatLng = new LatLng(Double.parseDouble(placeLat), Double.parseDouble(placeLng));
        }
    }

    public String getdYY() {
        return dYY;
    }

    public void setdYY(String dYY) {
        this.dYY = dYY;
    }

    public String getdMM() {
        return dMM;
    }

    public void setdMM(String dMM) {
        this.dMM = dMM;
    }

    public String getdDD() {
        return dDD;
    }

    public void setdDD(String dDD) {
        this.dDD = dDD;
    }

    public String getaYY() {
        return aYY;
    }

    public void setaYY(String aYY) {
        this.aYY = aYY;
    }

    public String getaMM() {
        return aMM;
    }

    public void setaMM(String aMM) {
        this.aMM = aMM;
    }

    public String getaDD() {
        return aDD;
    }

    public void setaDD(String aDD) {
        this.aDD = aDD;
    }

    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPersonCount() {
        return personCount;
    }

    public void setPersonCount(String personCount) {
        this.personCount = personCount;
    }

    public String getFirstPlace() {
        return firstPlace;
    }

    public void setFirstPlace(String firstPlace) {
        this.firstPlace = firstPlace;
    }

    public String getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(String placeLat) {
        this.placeLat = placeLat;
    }

    public String getPlaceLng() {
        return placeLng;
    }

    public void setPlaceLng(String placeLng) {
        this.placeLng = placeLng;
    }

    public LatLng getPlaceLatLng() {
        return placeLatLng;
    }

    public void setPlaceLatLng(LatLng placeLatLng) {
        this.placeLatLng = placeLatLng;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getPlaceBitmapFilePath() {
        return placeBitmapFilePath;
    }

    public void setPlaceBitmapFilePath(String placeBitmapFilePath) {
        this.placeBitmapFilePath = placeBitmapFilePath;
    }
}
