package com.example.kyeon.myapplication;

import android.content.Intent;

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
    private String title;
    public static final String titleTag = "title_text";
    private String personCount;
    public static final String countTag = "person_count";

    public IntentData(Intent intent) {
        dYY = intent.getStringExtra(dYYTag);
        dMM = intent.getStringExtra(dMMTag);
        dDD = intent.getStringExtra(dDDTag);
        aYY = intent.getStringExtra(aYYTag);
        aMM = intent.getStringExtra(aMMTag);
        aDD = intent.getStringExtra(aDDTag);
        title = intent.getStringExtra(titleTag);
        personCount = intent.getStringExtra(countTag);
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
}
