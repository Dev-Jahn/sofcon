package com.example.kyeon.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MyTrip extends AppCompatActivity {

    final int ITEM_SIZE = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip);


        RecyclerView recyclerView =  findViewById(R.id.rec_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        List<Item> items = new ArrayList<>();
        Item[] item = new Item[ITEM_SIZE];
        item[0] = new Item(R.drawable.main_background, "Seoul", "1.1 ~ 1.10");
        item[1] = new Item(R.drawable.main_background, "Fukuoka", "4.3 ~ 4.10");
        item[2] = new Item(R.drawable.main_background, "Beijing", "5.1 ~ 5.6");
        item[3] = new Item(R.drawable.main_background, "Busan", "4.13 ~ 4.19");
        item[4] = new Item(R.drawable.main_background, "Tai", "7.4 ~ 7.11");

        for (int i = 0; i < ITEM_SIZE; i++) {
            items.add(item[i]);
        }

        recyclerView.setAdapter(new TripCardAdapter(getApplicationContext(), items, R.layout.activity_my_trip));

    }
}
