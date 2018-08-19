package com.example.kyeon.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TravelActivity extends AppCompatActivity {

    int id;
    String place;
    int people_count;
    int days;
    int smm, syy, sdd;
    int emm, eyy, edd;

    Toolbar toolbar;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        // tmp data sets
        days = 5;


        viewPager = findViewById(R.id.trip_pager);

        TripPagerAdapter adapter = new TripPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public static class DiaryholderFragment extends android.support.v4.app.Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public DiaryholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DiaryholderFragment newInstance(int sectionNumber) {
            DiaryholderFragment fragment = new DiaryholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_travel, container, false);
            RecyclerView recyclerView = rootView.findViewById(R.id.diary_recview);

            int index = getArguments().getInt(ARG_SECTION_NUMBER);

            List<String> place_names = new ArrayList<>();

            if(index == 0)
            {
                place_names.add("보라매 공원");
                place_names.add("시발");
                place_names.add("숭실머학교");
            }
            else if(index == 1)
            {
                place_names.add("부산대");
                place_names.add("라면맛집");
            }
            else if(index == 2)
            {
                place_names.add("aiqwdij");
                place_names.add("asodjadijadoiaw");
                place_names.add("asdasdasdu");
                place_names.add("asodjadijadoedjweohdwoed");
            }
            else if(index == 3)
            {
                place_names.add("asodr개새끼야");
            }
            else
            {
                place_names.add("아몰랑");
            }
            place_names.add("씨발왜안되");

            recyclerView.setAdapter(new DiaryAdapter(getContext(), place_names, R.layout.fragment_travel));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }



    public void packing()
    {
        id = 20142350;//user id
        place = "Seoul";//장소
        people_count = 4;//사람수
        smm = 8; syy = 2018; sdd = 13;//시작~
        emm = 8; eyy = 2018; edd = 16;//끝

        days = sdd - edd;// 몇일짜리인지

        DailyDiary diaries[] = new DailyDiary[days+1];//각 날별로 경유지들마다 리뷰들을 받아옴

        Date date;
        date = new Date();
        /*
        for (int i = 0; i <= days; i++) {

            for (int j = 0; j < place_counts[i]; j++) {
                diaries[i].set_review(j, 121314, 50, "최고의 장소",getDrawable(R.drawable.city_busan));// image is tmp
            }
        }
        */

        /*
        써야할 데이터 목록 = id, place, smm,syy,sdd, emm,eyy,edd, days
                             날짜별로 place_counts[days+1], diaries[days+1]
         */

    }

    public void shareOnInternet(View v)
    {
        // tmp dataset

    }



    class TripPagerAdapter extends FragmentPagerAdapter {

        public TripPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position){
            return DiaryholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return days;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return "day" + (position+1);
        }
    }

    private class DailyDiary
    {
        int place_count;//정유지 갯수
        int place[];//장소id들
        int score[];//리뷰점수
        String review[];//리뷰 텍스트
        Drawable img[];
        int yy,mm,dd;

        public DailyDiary (int count, int yy, int mm, int dd)
        {
            this.place_count = count;
            place = new int[place_count];
            score = new int[place_count];
            review = new String[place_count];
            img = new Drawable[place_count];
            this.yy = yy;
            this.mm = mm;
            this.dd = dd;
        }

        private void set_review(int index, int place_id, int score, String review, Drawable image)
        {
            this.place[index] = place_id;
            this.score[index] = score;
            this.review[index] = review;
            this.img[index] = image;
        }

        public int getPlace_count() {
            return this.place_count;
        }
    }


}
