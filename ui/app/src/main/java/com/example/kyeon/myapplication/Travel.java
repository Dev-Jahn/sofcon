package com.example.kyeon.myapplication;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Travel {

    int id;
    String place;
    int people_count;
    int days;
    int smm, syy, sdd;
    int emm, eyy, edd;
    ArrayList<Integer> places;
    int place_counts[];

    private class DailyDiary
    {
        int place_count;//정유지 갯수
        int place[];//장소id들
        int score[];//리뷰점수
        String review[];//리뷰 텍스트
        Drawable img[];

        public DailyDiary (int count)
        {
            this.place_count = count;
            place = new int[place_count];
            score = new int[place_count];
            review = new String[place_count];
            img = new Drawable[place_count];
        }

        private void set_review(int index, int place_id, int score, String review, Drawable image)
        {
            this.place[index] = place_id;
            this.score[index] = score;
            this.review[index] = review;
            this.img[index] = image;
        }


    }
}
