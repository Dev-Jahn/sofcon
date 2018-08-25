package com.example.kyeon.myapplication;

public class InfoWindowData {
    private String title;
    private String snippet;
    // deprecated
    private String score;
    private int order;
    private String placeID;

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

    @Deprecated
    public String getScore() {
        return score;
    }

    @Deprecated
    public void setScore(String score) {
        this.score = score;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Deprecated
    public String getPlaceID() {
        return placeID;
    }
    @Deprecated
    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }
}