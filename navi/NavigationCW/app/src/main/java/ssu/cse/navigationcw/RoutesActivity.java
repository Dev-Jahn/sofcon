package ssu.cse.navigationcw;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoutesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView mNoticeTextView;

    private GoogleMap mMap;
    private ArrayList<LatLng> listLocsToDraw;

    /**
     * Actually MIN_TIME, MIN_DISTANCE have no special mean in our code
     * But If needed, We can use this (If we have to detect user's location, etc)
     */
    // unit : ms
    private static final long MIN_TIME = 500;
    // unit : meter
    private static final long MIN_DISTANCE = 50;


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

        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        String bestProvider = locManager.getBestProvider(criteria, true);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locManager.requestLocationUpdates(bestProvider, MIN_TIME, MIN_DISTANCE, mLocListener);
            Location location = locManager.getLastKnownLocation(bestProvider);
            // We should remove update listener (update only once)
            locManager.removeUpdates(mLocListener);
            Toast.makeText(this, "location is updating...", Toast.LENGTH_LONG).show();
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

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String originString = "origin=" + origin.latitude + "," + origin.longitude;
        String destString = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";
        String mode = "mode=driving";

        String parameters = originString + "&" + destString + "&" + sensor + "&" + mode;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
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
            getCurrentLocation();
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
            // Base View Point
            curLatLng = new LatLng(37.525007, 126.971547);
            Toast.makeText(this, "location is not updated", Toast.LENGTH_LONG).show();
        } else {
            curLatLng = new LatLng(curLocation.getLatitude(), curLocation.getLongitude());
            Toast.makeText(this, "location is updated", Toast.LENGTH_LONG).show();

        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(curLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
           @Override
           public void onMapClick(LatLng latLng) {
                if(listLocsToDraw.size() > 1) {
                    listLocsToDraw.clear();
                    mMap.clear();
                }

                listLocsToDraw.add(latLng);
               // create a marker for starting location
               MarkerOptions options = new MarkerOptions();
               options.position(latLng);
               if(listLocsToDraw.size() == 1)
                   options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
               else if(listLocsToDraw.size() == 2)
                   options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

               mMap.addMarker(options);

               if(listLocsToDraw.size() >= 2) {
                   LatLng origin = (LatLng) listLocsToDraw.get(0);
                   LatLng dest = (LatLng) listLocsToDraw.get(1);

                   String url = getDirectionsUrl(origin, dest);
                   DownloadTask downloadTask = new DownloadTask();

                   downloadTask.execute(url);

               }

           }
        });


        // listLocsToDraw.add(startLatLng);
        // listLocsToDraw.add(destLatLng);
        // drawLinePath();
    }
/*
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
*/
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("DEBUG-Error", e.toString());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {

        @Override
        protected List<List<HashMap<String,String>>> doInBackground(String... jsonData) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jsonObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jsonObject);
            } catch(Exception e) {
                Log.d("DEBUG-Error", e.toString());
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            Log.d(".java", "result.size = " + result.size());

            for(int i = 0; i < result.size(); ++i) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for(int j = 0; j < path.size(); ++j) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);
            }
            mMap.addPolyline(lineOptions);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
