package com.example.kyeon.myapplication;

import android.drm.DrmStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    Toolbar mainToolbar;
    DrawerLayout drawerLayout;
    LinearLayout navigation_main_background;
    private ListView navigationView;
    private String[] navItems = {"메인 메뉴", "새 여행", "내 여행", "일지","기타"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigation_main_background = (LinearLayout)findViewById((R.id.navigation_main_background));

        mainToolbar = (Toolbar)findViewById(R.id.maintoolbar);
        mainToolbar.setTitle("Hello World!!");
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.outline_list_black_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView = (ListView)findViewById(R.id.navigation_contents);
        navigationView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        navigationView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i) {
                    case 0:
                        break;

                    case 1:
                        intent = new Intent(
                                getApplicationContext(),
                                NewActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(
                                getApplicationContext(),
                                TravelActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        System.out.println(2);
                        break;
                    case 4:
                        System.out.println(3);
                        break;
                }
                drawerLayout.closeDrawer(navigation_main_background);
            }
        });

        Button bNewTrip = (Button)findViewById(R.id.newTrip);
        bNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        NewActivity.class);
                startActivity(intent);
            }
        });

        Button bMyTrip = (Button)findViewById(R.id.myTrip);
        bMyTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        TravelActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
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


