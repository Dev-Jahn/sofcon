package com.example.kyeon.myapplication;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class OthersTravel extends AppCompatActivity {

    DrawerLayout drawerLayout;
    LinearLayout navigation_others_travel_background;
    private ListView navigationView;
    private String[] navItems = {"메인 메뉴", "새 여행", "내 여행", "다른 여행","추천 여행"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_travel);

        Toolbar otherToolbar;


        drawerLayout = (DrawerLayout)findViewById(R.id.others_travel_drawer);
        navigation_others_travel_background = (LinearLayout)findViewById(R.id.navigation_others_travel_background);
        otherToolbar = (Toolbar)findViewById(R.id.OthersTravelToolbar);
        otherToolbar.setTitle("다른 여행");
        setSupportActionBar(otherToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.outline_list_black_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView = (ListView)findViewById(R.id.navigation_contents_from_OT);
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
                        intent = new Intent(
                                getApplicationContext(),
                                MyTrip.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 3:
                        break;
                    case 4:
                        intent = new Intent(
                                getApplicationContext(),
                                RecommendTravel.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(navigation_others_travel_background);
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
