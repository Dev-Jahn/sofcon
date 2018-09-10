package com.example.kyeon.myapplication;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyTripActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    LinearLayout navigation_MyTrip_background;
    private ListView navigationView;
    private String[] navItems = {"메인 메뉴", "새 여행", "내 여행", "다른 여행","추천 여행"};
    final int ITEM_SIZE = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip);

        Toolbar MyToolbar;

        RecyclerView recyclerView =  findViewById(R.id.rec_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        List<Item> items = new ArrayList<>();
        Item[] item = new Item[ITEM_SIZE];
        item[0] = new Item(R.drawable.city_seoul,"신나게 떠나는 서울여행", "Seoul", "1.1 ~ 1.10");
        item[1] = new Item(R.drawable.city_fukuoka,"열심히 쇼핑하다오는 후쿠오카", "Fukuoka", "4.3 ~ 4.10");
        item[2] = new Item(R.drawable.city_beijing, "옛 중국의 향기를 맡으러","Beijing", "5.1 ~ 5.6");
        item[3] = new Item(R.drawable.city_busan,"여름에 가기좋은 부산여행", "Busan", "4.13 ~ 4.19");
        item[4] = new Item(R.drawable.city_tai,"태국의 정취를 맡으러", "Tai", "7.4 ~ 7.11");

        for (int i = 0; i < ITEM_SIZE; i++) {
            items.add(item[i]);
        }

        recyclerView.setAdapter(new TripCardAdapter(getApplicationContext(), items, R.layout.activity_my_trip));

        drawerLayout = (DrawerLayout)findViewById(R.id.MyTrip_drawer);
        navigation_MyTrip_background = (LinearLayout)findViewById(R.id.navigation_MyTrip_background);
        MyToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.MyTripToolbar);
        MyToolbar.setTitle("내 여행");
        setSupportActionBar(MyToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.outline_list_black_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView = (ListView)findViewById(R.id.navigation_contents_from_MT);
        navigationView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        navigationView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i) {
                    case 0:
                        finish();
                        break;

                    case 1:
                        intent = new Intent(
                                getApplicationContext(),
                                NewActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case 2:
                        break;
                    case 3:
                        intent = new Intent(
                                getApplicationContext(),
                                OthersActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 4:
                        intent = new Intent(
                                getApplicationContext(),
                                RecommendTravel.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(navigation_MyTrip_background);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
