package com.example.kyeon.myapplication;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class TravelActivity extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

    }

    public void packing()
    {
        id = 20142350;//user id
        place = "Seoul";//장소
        people_count = 4;//사람수
        smm = 8; syy = 2018; sdd = 13;//시작~
        emm = 8; eyy = 2018; edd = 16;//끝

        days = sdd - edd;// 몇일짜리인지
        place_counts = new int[days+1];//각 날마다 경유지의 갯수
        place_counts[0] = 2;
        place_counts[1] = 4;
        place_counts[2] = 2;
        place_counts[3] = 4;

        DailyDiary diaries[] = new DailyDiary[days+1];//각 날별로 경유지들마다 리뷰들을 받아옴

        for (int i = 0; i <= days; i++) {

            for (int j = 0; j < place_counts[i]; j++) {
                diaries[i].set_review(j, 121314, 50, "최고의 장소",getDrawable(R.drawable.city_busan));// image is tmp
            }
        }

        /*
        써야할 데이터 목록 = id, place, smm,syy,sdd, emm,eyy,edd, days
                             날짜별로 place_counts[days+1], diaries[days+1]
         */

    }

    public void shareOnInternet(View v)
    {
        // tmp dataset

    }
}
