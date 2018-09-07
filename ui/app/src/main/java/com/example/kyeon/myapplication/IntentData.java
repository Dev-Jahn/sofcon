package com.example.kyeon.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class IntentData {

    private String dYY;
    public static final String dYYTag = "departing_year";
    private String dMM;
    public static final String dMMTag = "departing_month";
    private String dDD;
    public static final String dDDTag = "departing_day";
    private String aYY;
    public static final String aYYTag = "arriving_year";
    private String aMM;
    public static final String aMMTag = "arriving_month";
    private String aDD;
    public static final String aDDTag = "arriving_day";
    private String currentDay;
    public static final String currentDayTag = "currentDay";
    private String title;
    public static final String titleTag = "title_text";
    private String personCount;
    public static final String countTag = "person_count";
    private String firstPlace;
    public static final String firstPlaceTag = ChooseFirstPlaceActivity.PLACE_NAME;
    private String placeLat;
    public static final String placeLatTag = ChooseFirstPlaceActivity.PLACE_LAT;
    private String placeLng;
    public static final String placeLngTag = ChooseFirstPlaceActivity.PLACE_LNG;
    private String placeType;
    public static final String placeTypeTag = ChooseFirstPlaceActivity.PLACE_TYPE;
    private Bitmap placeBitmap;
    public static final String placeBitmapTag = ChooseFirstPlaceActivity.PLACE_BITMAP;
    private LatLng placeLatLng;

    public IntentData(Intent intent) {
        dYY = intent.getStringExtra(dYYTag);
        dMM = intent.getStringExtra(dMMTag);
        dDD = intent.getStringExtra(dDDTag);
        aYY = intent.getStringExtra(aYYTag);
        aMM = intent.getStringExtra(aMMTag);
        aDD = intent.getStringExtra(aDDTag);
        currentDay = intent.getStringExtra(currentDayTag);
        title = intent.getStringExtra(titleTag);
        personCount = intent.getStringExtra(countTag);
        firstPlace = intent.getStringExtra(firstPlaceTag);
        placeLat = intent.getStringExtra(placeLatTag);
        placeLng = intent.getStringExtra(placeLngTag);
        calcLatLng();
        placeType = intent.getStringExtra(placeTypeTag);
        try {
            placeBitmap = (Bitmap) intent.getExtras().get(placeBitmapTag);
        } catch(NullPointerException e) {
            Log.d(".java", "비트맵 이미지 전달 오류 in IntentData");
        }
    }

    protected void transferDataToIntent(Intent intent) {
        intent.putExtra(dYYTag, dYY);
        intent.putExtra(dMMTag, dMM);
        intent.putExtra(dDDTag, dDD);
        intent.putExtra(aYYTag, aYY);
        intent.putExtra(aMMTag, aMM);
        intent.putExtra(aDDTag, aDD);
        intent.putExtra(currentDayTag, currentDay);
        intent.putExtra(titleTag, title);
        intent.putExtra(firstPlaceTag, firstPlace);
        intent.putExtra(placeLatTag, placeLat);
        intent.putExtra(placeLngTag, placeLng);
        intent.putExtra(placeTypeTag, placeType);
        intent.putExtra(placeBitmapTag, placeBitmap);
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

    public Bitmap getPlaceBitmap() {
        return placeBitmap;
    }

    public void setPlaceBitmap(Bitmap placeBitmap) {
        this.placeBitmap = placeBitmap;
    }
}
