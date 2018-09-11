package com.example.kyeon.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

;import java.io.File;

public class MainActivity extends AppCompatActivity{

    Toolbar mainToolbar;
    SearchView mSearchView;
    DrawerLayout drawerLayout;
    LinearLayout navigation_main_background;
    private ListView navigationView;
    private String[] navItems = {"메인 메뉴", "새 여행", "내 여행", "다른 여행"/*,"추천 여행"*/};

    ImageView blnkImage;
    LinearLayout nav_image_text;
    Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigation_main_background = (LinearLayout)findViewById((R.id.navigation_main_));

        mainToolbar = (Toolbar)findViewById(R.id.maintoolbar);
        mainToolbar.setTitle("WELCOME TO WANDER");
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.outline_list_black_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView = (ListView)findViewById(R.id.navigation_contents_from_main);
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
                        /*intent = new Intent(
                                getApplicationContext(),
                                MyTripFragment.class);
                        startActivity(intent);*/
                        break;
                    case 3:
                        /*intent = new Intent(
                                getApplicationContext(),
                                OthersFragment.class);
                        startActivity(intent);*/
                        break;
                    /*case 4:
                        intent = new Intent(
                                getApplicationContext(),
                                RecommendTravel.class);
                        startActivity(intent);
                        break;*/
                }
                drawerLayout.closeDrawer(navigation_main_background);
            }
        });
        /*




*/

        //places that bottomnavigation behavior appears
        BottomNavigationView btm_navigation = findViewById(R.id.btm_navigation);
        btm_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId())
                {
                    case R.id.action_my:
                        selectedFragment = MyTripFragment.newInstance();
                        mainToolbar.getMenu().clear();
                        mainToolbar.inflateMenu(R.menu.menu);
                        break;
                    case R.id.action_home:
                        selectedFragment = HomeFragment.newInstance();
                        mainToolbar.getMenu().clear();
                        mainToolbar.inflateMenu(R.menu.menu);
                        break;
                    case R.id.action_others:
                        Toast.makeText(MainActivity.this, "구현중.", Toast.LENGTH_SHORT).show();;
                        mainToolbar.setTitle("다른 여행");

                        mainToolbar.inflateMenu(R.menu.search);
                        mSearchView = (SearchView) mainToolbar.getMenu().findItem(R.id.menu_search).getActionView();

                        selectedFragment = OthersFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });
        
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_layout, HomeFragment.newInstance());
        transaction.commit();


        /*

        Button bNewTrip = (Button)findViewById(R.id.newTrip);
        final Drawable button_transparency_bNewTrip = bNewTrip.getBackground();
        button_transparency_bNewTrip.setAlpha(255);
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
        Drawable button_transparency_bMyTrip = bMyTrip.getBackground();
        button_transparency_bMyTrip.setAlpha(255);

        bMyTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        MyTripFragment.class);
                startActivity(intent);
            }
        });

        Button bOtherTrip = (Button)findViewById(R.id.otherTrip);
        Drawable button_transparency_bOtherTrip = bOtherTrip.getBackground();
        button_transparency_bOtherTrip.setAlpha(255);

        bOtherTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        OthersActivity.class);
                startActivity(intent);
            }
        });

        Button bRecommendTrip = (Button)findViewById(R.id.recommendTrip);
        Drawable button_transparency_bRecommendTrip = bRecommendTrip.getBackground();
        button_transparency_bRecommendTrip.setAlpha(255);

        bRecommendTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        RecommendTravel.class);
                startActivity(intent);
            }
        });
        */

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
                mainToolbar = (Toolbar)findViewById(R.id.maintoolbar);
                drawerLayout.openDrawer(GravityCompat.START);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("종료 확인 대화 상자")
                .setMessage("앱을 종료 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
}


