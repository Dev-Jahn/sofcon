package com.example.kyeon.myapplication;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    Toolbar mainToolbar;
    int c1, c2;
    final int BACKGROUND = 0;
    final int FOREGROUND = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = (Toolbar)findViewById(R.id.maintoolbar);
        mainToolbar.setTitle("Hello World!!");
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.outline_list_black_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

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
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
