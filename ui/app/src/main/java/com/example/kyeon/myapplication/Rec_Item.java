package com.example.kyeon.myapplication;

public class Rec_Item {
    int image;
    String title;
    String content;

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

    Rec_Item(int image, String title, String content)
    {
        this.image = image;
        this.title = title;
        this.content = content;
    }
}
