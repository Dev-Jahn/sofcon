package com.example.kyeon.myapplication;

public class Rec_Item {
    int image;
    String title;
    String content;
    int color;

    public int getImage() {
        return this.image;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getContent()
    {
        return this.content;
    }

    public int getColor() { return  this.color;}

    Rec_Item(int image, String title, String content, int color)
    {
        this.image = image;
        this.title = title;
        this.content = content;
        this.color = color;
    }
}
