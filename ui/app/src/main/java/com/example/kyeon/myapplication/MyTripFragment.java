package com.example.kyeon.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyTripFragment extends Fragment {

    DrawerLayout drawerLayout;
    LinearLayout navigation_MyTrip_background;
    private ListView navigationView;
    private String[] navItems = {"메인 메뉴", "새 여행", "내 여행", "다른 여행","추천 여행"};
    final int EXAMPLE_CASES = 4;
    public static MyTripFragment newInstance() {
        MyTripFragment fragment = new MyTripFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_my_trip, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.rec_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<Item> items = new ArrayList<>();
        Item[] item = new Item[EXAMPLE_CASES];
        item[0] = new Item(R.drawable.city_seoul, "신나게 떠나는 서울여행", "Seoul", "1.1 ~ 1.10", null);
        item[1] = new Item(R.drawable.city_fukuoka, "열심히 쇼핑하다오는 후쿠오카", "Fukuoka", "4.3 ~ 4.10", null);
        item[2] = new Item(R.drawable.city_beijing, "옛 중국의 향기를 맡으러", "Beijing", "5.1 ~ 5.6", null);
        item[3] = new Item(R.drawable.city_busan, "여름에 가기좋은 부산여행", "Busan", "4.13 ~ 4.19", null);

        for (int i = 0; i < EXAMPLE_CASES; i++) {
            items.add(item[i]);
        }
        //for testing travel class
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TEST/";
        Travel travel;
        ArrayList<Integer> drawables = new ArrayList<>();
        drawables.add(R.drawable.and_img1);
        drawables.add(R.drawable.and_img2);
        drawables.add(R.drawable.and_img3);
        drawables.add(R.drawable.and_img4);
        drawables.add(R.drawable.and_img5);
        drawables.add(R.drawable.and_img6);
        drawables.add(R.drawable.and_img7);
        drawables.add(R.drawable.and_img8);
        try
        {
            int travelCount = 1;
            File file;
            file = getContext().getFileStreamPath("travel_"+travelCount);
            while(file.exists())
            {
                travel = Travel.load(getContext(), "travel_"+travelCount);
                items.add(new Item(drawables.get(travelCount-1), travel.cityName,travel.title,
                        travel.syy + "." + travel.smm + "." + travel.sdd + " ~ " +
                                travel.eyy +"."+travel.emm+"."+travel.edd, travel
                ));
                travelCount++;
            }

        }catch (Exception e)
        {
            travel = null;
            Toast.makeText(getContext(), "sibal", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        //



        recyclerView.setAdapter(new TripCardAdapter(getContext(), items, R.layout.fragment_my_trip));

        FloatingActionButton btn_add = rootView.findViewById(R.id.btn_add_floating);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NewActivity.class);
                startActivity(i);
            }
        });

        return rootView;
    }

}
