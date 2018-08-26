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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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

    @Deprecated
    protected static Location getCurrentLocation(final Context context, final Activity activity) {

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
    @Deprecated
    protected static void resetCameraLocation(GoogleMap mMap, Context context, Activity activity) {

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

    /**
     * Below codes will be coded soon...
     * ASAP...
     */

    /**
     * Big Problem!!!
     * -----------------
     * 1. How can I save these things?
     * 2. How can I name file?
     * 3. How can I load map instance using filename?
     *    --> In loadMapInstance, argument fileName is hard to receive from other methods
     */
    protected static void saveMapInstance(Context context, GoogleMap mMap, ArrayList<Marker> markers, String tripTitle, int day) {
        /**
         * It should be called in onPause
         */
        try {
            String fileName = tripTitle + Integer.toString(day) + ".dat";
            // Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeInt(markers.size()); // Save line count
            /**
            for (LatLng point : markers) {
                dos.writeUTF(point.latitude + "," + point.longitude);
                Log.v("write", point.latitude + "," + point.longitude);
            }
             */
            for (Marker marker : markers) {
                LatLng position = marker.getPosition();
                /**
                 * File content format
                 * --
                 * lat,lng,title,snippet,score,order,placeID
                 * --
                 * If needed, I can add more information
                 */
                String content = position.latitude + "," + position.longitude + ","
                        + ((InfoWindowData)marker.getTag()).toString();
                dos.writeUTF(content);
                Log.v("DEBUG-FILEIO-WRITE", content);
                Log.v("DEBUG-FILEIO-WRITE", ((InfoWindowData)marker.getTag()).toString());
            }
            dos.flush(); // Flush stream ...
            dos.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    protected static void loadMapInstance(Context context, GoogleMap mMap, String fileName) {
        /**
         * It should be called in onResume
         */
        try {
            FileInputStream input = context.openFileInput(fileName);
            DataInputStream din = new DataInputStream(input);
            int sz = din.readInt(); // Read line count
            for (int i = 0; i < sz; i++) {
                String str = din.readUTF();
                Log.v("DEBUG-FILEIO-READ", str);
                String[] stringArray = str.split(",");
                double latitude = Double.parseDouble(stringArray[0]);
                double longitude = Double.parseDouble(stringArray[1]);
                LatLng latLng = new LatLng(latitude, longitude);
                InfoWindowData infoWindowData = new InfoWindowData();
                infoWindowData.setTitle(stringArray[2]);
                infoWindowData.setSnippet(stringArray[3]);
                infoWindowData.setScore(stringArray[4]);
                infoWindowData.setOrder(Integer.parseInt(stringArray[5]));
                infoWindowData.setPlaceID(stringArray[6]);
                //listOfPoints.add(new LatLng(latitude, longitude));
            }
            din.close();
            //loadMarkers(listOfPoints);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    protected static class FindPlacesTask extends AsyncTask<String, Void, String> {
        String result;
        String url, lat, lon;
        float len;

        public FindPlacesTask(String lat, String lon, float len) {
            this.lon = lon;
            this.lat = lat;
            this.len = len;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // long start = System.currentTimeMillis();
                url = "http://35.189.138.177:8080/navi/findPlace?lat=" + lat + "&lon=" + lon + "&len=" + len;
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int retCode = conn.getResponseCode();

                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                br.close();

                result = response.toString();
                long end = System.currentTimeMillis();
                // Log.d(String.valueOf((end - start) / 1000.0), "Mylog");
                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d("DEBUG-TEST", s.toString());
        }
    }

    protected static ArrayList<PlaceData> placeParsing(String jsonPlaces) {
        try {
            JSONArray jsonArray = new JSONArray(jsonPlaces);
            ArrayList<PlaceData> arrayList = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PlaceData placeData = new PlaceData();
                placeData.setType(jsonObject.getString(PlaceData.jsonType));
                placeData.setName(jsonObject.getString(PlaceData.jsonName));
                placeData.setLat(jsonObject.getString(PlaceData.jsonLat));
                placeData.setLng(jsonObject.getString(PlaceData.jsonLng));
                arrayList.add(placeData);
            }
            return arrayList;
        } catch(JSONException e) {
            Log.d("DEBUG-ERROR", e.getMessage());
        }
        return null;
    }
}
