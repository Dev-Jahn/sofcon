package com.example.kyeon.myapplication;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class ChoosePlacesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PLACE_PICKER_REQUEST = 12;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_places);
        ImageButton test = (ImageButton) findViewById(R.id.closeButton);
<<<<<<< HEAD
=======

>>>>>>> 0d42699f5252927500346436547459b4202f8d61
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ChoosePlacesActivity.this.overridePendingTransition(R.anim.stay, R.anim.sliding_down);
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

        LatLng curLatLng;

        Location curLocation = MapUtility.getCurrentLocation(this, this);
        if(curLocation == null) {
            // Base View Point
            curLatLng = new LatLng(37.525007, 126.971547);
            // Below code is just debug code, delete it if test is over
            Toast.makeText(this, "location is not updated", Toast.LENGTH_LONG).show();
            /**
             * If needed, we can request to users to enable their GPS
             * Reference codes at MapsActivity
             */
        } else {
            curLatLng = new LatLng(curLocation.getLatitude(), curLocation.getLongitude());
            // Below code is just debug code, delete it if test is over
            Toast.makeText(this, "location is updated", Toast.LENGTH_LONG).show();
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(curLatLng));
        // We must discuss about default camera zoom level
        // I think level 16 is appropriate
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

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
}
