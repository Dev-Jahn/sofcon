package com.example.kyeon.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class OthersFragment extends Fragment {

    final int ITEM_SIZE = 5;
    public static OthersFragment newInstance() {
        OthersFragment fragment = new OthersFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_others, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rec_view_others);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<TripOthersItem> items = new ArrayList<>();
        items.add(new TripOthersItem(R.drawable.city_busan,R.drawable.sample_image,"psm", "마 붓싼 아이가", "Busan", "2019 0405 ~ 2019 0407",123));
        items.add(new TripOthersItem(R.drawable.city_tai,R.drawable.sample_image,"axs", "마 타이 아이가", "tai", "2019 0407 ~ 2019 0412",3884));
        items.add(new TripOthersItem(R.drawable.city_fukuoka,R.drawable.sample_image,"wadij", "마 후쿠오카 아이가", "fukuoka", "2019 0405 ~ 2019 0407", 332));


        recyclerView.setAdapter(new OthersTripAdapter(getContext(), items, R.layout.fragment_others));
        return rootView;
    }

}
