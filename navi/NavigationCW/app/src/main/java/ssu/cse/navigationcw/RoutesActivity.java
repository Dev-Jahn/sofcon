package ssu.cse.navigationcw;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class RoutesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView mNoticeTextView;

    private GoogleMap mMap;
    private ArrayList<LatLng> listLocsToDraw;
    // unit : ms
    private static final long MIN_TIME = 500;
    // unit : meter
    private static final long MIN_DISTANCE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        mNoticeTextView = (TextView)findViewById(R.id.noticeTextView);

        listLocsToDraw = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);



        mapFragment.getMapAsync(this);
    }

    public Location getCurrentLocation() {

        Criteria criteria = new Criteria();
        // Accuracy of Current location
        criteria.setAccuracy(Criteria.ACCURACY_HIGH);
        // We do not care about battery consumption
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        // We do not need altitude
        criteria.setAltitudeRequired(false);
        // We do not need direction
        criteria.setBearingRequired(false);
        // We do not care about speed
        criteria.setSpeedRequired(false);

        // If needed, we can show some messages to LTE users warning their network cost
        criteria.setCostAllowed(true);


        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        String bestProvider = locManager.getBestProvider(criteria, true);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locManager.requestLocationUpdates(bestProvider, MIN_TIME, MIN_DISTANCE, mLocListener);
            Location location = locManager.getLastKnownLocation(bestProvider);
            return location;
        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        PermissionCodes.REQUEST_CODE_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                        PermissionCodes.REQUEST_CODE_COARSE_LOCATION);
            }

        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PermissionCodes.REQUEST_CODE_FINE_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // IF permission was allowed

            } else {
                // IF Permission was denied or request was cancelled
            }
        } else if (requestCode == PermissionCodes.REQUEST_CODE_COARSE_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // IF permission was allowed

            } else {
                // IF Permission was denied or request was cancelled
            }
        }
    }

    private LocationListener mLocListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng curLatLng;
        Location curLocation = getCurrentLocation();
        if(curLocation == null) {
            curLatLng = new LatLng(curLocation.getLatitude(), curLocation.getLongitude());
        } else {
            // Base View Point
            curLatLng = new LatLng(37.525007, 126.971547);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(curLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        // listLocsToDraw.add(startLatLng);
        // listLocsToDraw.add(destLatLng);

        drawLinePath();
    }

    private void drawLinePath()
    {
        if(mMap == null)
        {
            return;
        }
        if(listLocsToDraw.size() < 2)
        {
            return;
        }

        PolylineOptions options = new PolylineOptions();

        options.color(Color.parseColor("#CC0000FF"));
        options.width(5);
        options.visible(true);

        for(LatLng locRecorded : listLocsToDraw)
        {
            options.add(locRecorded);
        }
        mMap.addPolyline(options);
    }
}
