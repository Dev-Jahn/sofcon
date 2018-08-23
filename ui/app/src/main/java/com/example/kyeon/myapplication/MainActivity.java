package com.example.kyeon.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

;

public class MainActivity extends AppCompatActivity{

    Toolbar mainToolbar;
    int c1;
    DrawerLayout drawerLayout;
    LinearLayout navigation_main_background;
    private ListView navigationView;
    private String[] navItems = {"메인 메뉴", "새 여행", "내 여행", "일지","기타"};

    ImageView blnkImage;
    LinearLayout nav_image_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigation_main_background = (LinearLayout)findViewById((R.id.navigation_main_));

        mainToolbar = (Toolbar)findViewById(R.id.maintoolbar);
        mainToolbar.setTitle("메인 메뉴");
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

        AnimationSet animationSet = new AnimationSet(true);

        Animation alpha = new AlphaAnimation(0.2f, 1.0f);
        alpha.setDuration(1500);

        Animation slide_up = new TranslateAnimation(0,0,100,0);
        slide_up.setDuration(1500);

        animationSet.addAnimation(alpha);
        animationSet.addAnimation(slide_up);
        final ImageView imageView = findViewById(R.id.imageView);
        imageView.startAnimation(animationSet);

        final CircleImageView circleImageView = findViewById(R.id.main_image);
        final CircleImageView circleImageView_background = findViewById(R.id.main_image_background);
        final Drawable backgrounds[] = new Drawable[5];
        backgrounds[0] = getDrawable(R.drawable.city_busan);
        backgrounds[1] = getDrawable(R.drawable.city_seoul);
        backgrounds[2] = getDrawable(R.drawable.city_fukuoka);
        backgrounds[3] = getDrawable(R.drawable.city_beijing);
        backgrounds[4] = getDrawable(R.drawable.city_tai);

        final Animation fade_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        final Animation fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        circleImageView.setAnimation(fade_out);// 한번 부름
        circleImageView_background.setAnimation(fade_in);
        c1 = 1;

        fade_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("sibal", "onAnimationEnd: c : " + c1);

                if(c1 % 2 == 0)
                {
                    Random rand = new Random();
                    int index = rand.nextInt(backgrounds.length);
                    circleImageView_background.setImageDrawable(backgrounds[index]);
                    circleImageView_background.startAnimation(fade_in);
                    animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                    animation.setDuration(1);
                    circleImageView.startAnimation(animation);
                    circleImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            circleImageView.startAnimation(fade_out);
                        }
                    });
                    c1++;
                }
                else
                {
                    animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                    animation.setDuration(1);
                    circleImageView_background.startAnimation(animation);
                    circleImageView_background.post(new Runnable() {
                        @Override
                        public void run() {
                            circleImageView_background.startAnimation(fade_out);
                        }
                    });
                    Random rand = new Random();
                    int index = rand.nextInt(backgrounds.length);
                    circleImageView.setImageDrawable(backgrounds[index]);
                    circleImageView.startAnimation(fade_in);
                    c1++;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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
                        MyTrip.class);
                startActivity(intent);
            }
        });

        Button bOtherTrip = (Button)findViewById(R.id.otherTrip);
        Drawable button_transparency_bOtherTrip = bOtherTrip.getBackground();
        button_transparency_bOtherTrip.setAlpha(255);

        Button bRecommendTrip = (Button)findViewById(R.id.recommendTrip);
        Drawable button_transparency_bRecommendTrip = bRecommendTrip.getBackground();
        button_transparency_bRecommendTrip.setAlpha(255);

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


