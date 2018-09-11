package com.example.kyeon.myapplication;

public class Item {
    int image;
    String title;
    String place;
    String day;
    Travel travel;

    public int getImage() {
        return this.image;
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

    public Travel getTravel() { return this.travel; }
    Item(int image, String title, String place, String day, Travel travel)
    {
        this.title = title;
        this.image = image;
        this.place = place;
        this.day = day;
        this.travel = travel;
    }
}
