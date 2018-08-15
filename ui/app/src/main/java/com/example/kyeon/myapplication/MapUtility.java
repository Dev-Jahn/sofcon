package com.example.kyeon.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MapUtility {

    /**
     * Actually MIN_TIME, MIN_DISTANCE have no special mean in our code
     * But If needed, We can use this (If we have to detect user's location, etc)
     */
    // unit : ms
    private static final long MIN_TIME = 500;
    // unit : meter
    private static final long MIN_DISTANCE = 50;
    // We must discuss about default camera zoom level
    // I think level 16 is appropriate
    protected static final int ZOOM_LEVEL = 16;
    // Current default location is soongsil univ.
    protected static final LatLng DEFAULT_LOCATION = new LatLng(37.495999, 126.957050);

    public static Location getCurrentLocation(final Context context, final Activity activity) {

        Criteria criteria = new Criteria();
        // Accuracy of Current location
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
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


        LocationManager locManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        LocationListener mLocListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getCurrentLocation(context, activity);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                getCurrentLocation(context, activity);
            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        String bestProvider = locManager.getBestProvider(criteria, true);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locManager.requestLocationUpdates(bestProvider, MIN_TIME, MIN_DISTANCE, mLocListener);
            Location location = locManager.getLastKnownLocation(bestProvider);
            // We should remove update listener (update only once)
            locManager.removeUpdates(mLocListener);
            // Below code is just debug code, delete it if test is over
            //Toast.makeText(context, "location is updating...", Toast.LENGTH_LONG).show();
            return location;
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PermissionCodes.REQUEST_CODE_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PermissionCodes.REQUEST_CODE_COARSE_LOCATION);
            }

        }
        return null;
    }

    public static void resetCameraLocation(GoogleMap mMap, Context context, Activity activity) {

        Resources resources = context.getResources();

        if(mMap == null) {
            String errorMsg = resources.getString(R.string.cameraResetErrorMessage);
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
        }
        else {
            LatLng curLatLng;

            Location curLocation = getCurrentLocation(context, activity);
            if (curLocation == null) {
                // Base View Point - hard coded location will be replaced
                curLatLng = DEFAULT_LOCATION;
                // Below code is just debug code, delete it if test is over
                String errorMsg = resources.getString(R.string.locUpdateErrorMessage);
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                /**
                 * If needed, we can request to users to enable their GPS
                 * Reference codes at MapsActivity
                 */
            } else {
                curLatLng = new LatLng(curLocation.getLatitude(), curLocation.getLongitude());
                // Below code is just debug code, delete it if test is over
                String msg = resources.getString(R.string.cameraResetMessage);
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLng(curLatLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
        }
    }

}
