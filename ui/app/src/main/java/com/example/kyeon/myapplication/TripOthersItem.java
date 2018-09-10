package com.example.kyeon.myapplication;

public class TripOthersItem {
    int image;
    int userImage;
    int views;
    String title;
    String place;
    String userId;
    String day;

    public int getImage() {
        return this.image;
    }

    public int getUserImage() {
        return this.userImage;
    }

    public String getUserId() {
        return this.userId;
    }

    public int getViews() {
        return this.views;
    }

    public String getDay() {
        return this.day;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPlace() {
        return this.place;
    }
    TripOthersItem(int image,int userImage, String userId, String title, String place, String day, int views)
    {
        this.userId = userId;
        this.userImage = userImage;
        this.title = title;
        this.image = image;
        this.place = place;
        this.day = day;
        this.views = views;
    }
}
