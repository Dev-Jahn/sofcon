package com.example.kyeon.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class ChoosePlacesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_places);

        ImageButton test = (ImageButton) findViewById(R.id.closeButton);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ChoosePlacesActivity.this.overridePendingTransition(R.anim.stay, R.anim.sliding_down);
            }
        });

        Button locationButton = (Button)findViewById(R.id.locationButton);
        Button cancelButton = (Button)findViewById(R.id.cancelButton);
        Button selectButton = (Button)findViewById(R.id.selectButton);

        locationButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapUtility.resetCameraLocation(mMap, getContext(), getActivity());
            }
        });

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
            }
        });

        selectButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * must write finish codes . . .
                 * DO NOT forget about it
                 */
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * First lifecycle of google map
     * @param googleMap
     * @author archslaveCW
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MapUtility.resetCameraLocation(mMap, getContext(), getActivity());

        /**
         * map click listener
         */
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                /**
                 * to do list
                 * 1. choose place by place picker?
                 * 2. get place
                 */
            }
        });
    }

    /**
     * Ugly code... sorry
     * @author archslaveCW
     */
    private Context getContext() { return this; }
    private Activity getActivity() { return this; }
}
