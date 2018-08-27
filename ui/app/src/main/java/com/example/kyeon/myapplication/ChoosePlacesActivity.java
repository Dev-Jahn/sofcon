package com.example.kyeon.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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
import java.util.concurrent.ExecutionException;

/**
 * @author archslaveCW
 */

public class ChoosePlacesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // Received intent data
    private IntentData intentData;
    // Locations to draw
    private ArrayList<LatLng> listLocsToDraw = new ArrayList<>();
    private ArrayList<LatLng> listLocsOfPlaces = new ArrayList<>();
    private ArrayList<Marker> listMarkersToSave = new ArrayList<>();
    private HashMap<Integer, Marker> hashMapUserMarker = new HashMap<>();
    private HashMap<Integer, Marker> hashMapPlaceMarker = new HashMap<>();
    private static int userMarkerCount = 0;
    private static int placeMarkerCount = 0;
    private View customMarkerOriginDestRoot;
    private TextView tvCustomMarkerOriginDest;
    private View customMarkerWayPointRoot;
    private TextView tvCustomMarkerWayPoint;

    private static final int markerHeight = 0;
    private static final int bottomOffset = 36;

    private MapWrapperLayout wrapperLayout;
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private ImageButton infoButton;
    private InfoWindowTouchListener infoWindowTouchListener;

    private String adjacencyPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_places);
        getIntentDatas();

        ImageButton closeImgButton = (ImageButton) findViewById(R.id.closeButton);

        closeImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ChoosePlacesActivity.this.overridePendingTransition(R.anim.stay, R.anim.sliding_down);
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        Button selectButton = (Button) findViewById(R.id.selectButton);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetMap();
            }
        });

        selectButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapUtility.saveMapUserMarkers(getContext(), mMap, listMarkersToSave, intentData.getTitle(), 1);
            }
        });

        customMarkerOriginDestRoot = LayoutInflater.from(this).inflate(R.layout.marker_custom_origin_dest, null);
        tvCustomMarkerOriginDest = (TextView) customMarkerOriginDestRoot.findViewById(R.id.custom_marker_text);
        customMarkerWayPointRoot = LayoutInflater.from(this).inflate(R.layout.marker_custom_waypoint, null);
        tvCustomMarkerWayPoint = (TextView) customMarkerWayPointRoot.findViewById(R.id.custom_marker_text);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /**
         *
         */
    }

    private void getIntentDatas() {
        Intent intent = getIntent();
        intentData = new IntentData(intent);
    }

    /**
     * First lifecycle of google map
     *
     * @param googleMap
     * @author archslaveCW
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        listLocsToDraw.clear();
        hashMapUserMarker.clear();
        hashMapPlaceMarker.clear();

        wrapperLayout = (MapWrapperLayout) findViewById(R.id.wrapperLayout);
        wrapperLayout.init(mMap, MapWrapperLayout.getPixelsFromDp(getContext(), markerHeight + bottomOffset));

        infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.marker_window, null);
        this.infoTitle = (TextView) infoWindow.findViewById(R.id.tvTitle);
        this.infoSnippet = (TextView) infoWindow.findViewById(R.id.tvSnippet);
        this.infoButton = (ImageButton) infoWindow.findViewById(R.id.ibDelete);

        infoWindowTouchListener = new InfoWindowTouchListener(infoButton) {
            @Override
            protected void onClickConfirmed(View v, final Marker marker) {
                //Toast.makeText(getContext(), "테스트 결과 이상 없음, 잘 눌림", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle(getResources().getString(R.string.remove_marker_title))
                        .setMessage(getResources().getString(R.string.remove_marker_description))
                        .setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.remove_marker_ok),
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeUserMarker(marker);
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.remove_marker_no),
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        };
        infoButton.setOnTouchListener(infoWindowTouchListener);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                infoTitle.setText(marker.getTitle());
                infoSnippet.setText(marker.getSnippet());
                infoWindowTouchListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                wrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        /**
         * map click listener
         */
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addUserMarker(latLng);
                if (listLocsToDraw.size() >= 2)
                    drawRoute();
            }
        });
/**
 *
 * ---> It has a big problem
 *      1. How can i use popup window smoothly?
 **/
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                // LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                // PopupMenu popupMenu = new PopupMenu(getApplicationContext(),
                marker.setTitle("TITLE of MARKER");
                marker.setSnippet("EXAMPLE SNIPPET");

                if (marker.isInfoWindowShown())
                    marker.showInfoWindow();

                /**
                 removeUserMarker(mMap, marker);
                 */
                return false;
            }
        });

        /**
         * camera move listener
         */
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
;               removePlaceMarker();
                /**
                 * to do list
                 * - request adjacency places to server
                 */
                String currentLat = new Double(cameraPosition.target.latitude).toString();
                String currentLng = new Double(cameraPosition.target.longitude).toString();
                float len = 1.5f;
                MapUtility.FindPlacesTask findPlacesTask = new MapUtility.FindPlacesTask(currentLat, currentLng, len);
                findPlacesTask.execute();
                try {
                    adjacencyPlaces = findPlacesTask.get();
                    Log.d("TESTTEST", adjacencyPlaces+" ");
                    if(adjacencyPlaces != null) {
                        ArrayList<PlaceData> placeDataArrayList = MapUtility.placeParsing(adjacencyPlaces);
                        if(placeDataArrayList != null) {
                            for(PlaceData placeData : placeDataArrayList) {
                                /**
                                boolean canAdd = true;
                                for(LatLng latLng : listLocsOfPlaces) {
                                    if(Double.parseDouble(placeData.getLat()) == latLng.latitude
                                        && Double.parseDouble(placeData.getLng()) == latLng.longitude) {
                                        continue;
                                    }
                                    else {
                                        canAdd = false;
                                        break;
                                    }
                                }
                                if(canAdd == true) {
                                    addPlaceMarker(placeData);
                                    listLocsOfPlaces.add(placeData.getLat())
                                }
                                 */
                                addPlaceMarker(placeData);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Below code is deprecated.
         */
        // MapUtility.resetCameraLocation(mMap, getContext(), getActivity());

        mLocationPermissionGranted = PermissionCodes.getPermission(getContext(), getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION, PermissionCodes.REQUEST_CODE_FINE_LOCATION);
        PermissionCodes.getPermission(getContext(), getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION, PermissionCodes.REQUEST_CODE_COARSE_LOCATION);
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /**
     * Deprecated.
     * Because of korea's fucking map policy
     * About 400 lines of code will be replaced to 10 lines of FUCKING straight line drawing code
     * T_T
     */
    @Deprecated
    private void drawRoute() {
        if (listLocsToDraw.size() >= 2) {
            LatLng origin = (LatLng) listLocsToDraw.get(listLocsToDraw.size() - 2);
            LatLng dest = (LatLng) listLocsToDraw.get(listLocsToDraw.size() - 1);

            if (userMarkerCount > 2) {
                Marker wayPointMarker = hashMapUserMarker.get(userMarkerCount - 1);
                tvCustomMarkerWayPoint.setText(new Integer(userMarkerCount - 1).toString());
                wayPointMarker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), customMarkerWayPointRoot)));
            }
            /**
             * requests draw a line for origin & dest
             */
            String url = getDirectionsUrl(origin, dest);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }
    }

    /**
     * Deprecated.
     * Because of korea's fucking map policy
     * About 400 lines of code will be replaced to 10 lines of FUCKING straight line drawing code
     * T_T
     */
    @Deprecated
    private void redrawRoute() {
        if (listLocsToDraw.size() >= 2) {
            LatLng origin;
            LatLng dest;
            for (int i = 0; i < listLocsToDraw.size() - 1; ++i) {
                origin = (LatLng) listLocsToDraw.get(i);
                dest = (LatLng) listLocsToDraw.get(i + 1);
                /**
                 * requests draw a line for origin & dest
                 */
                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            }
        }
    }

    private void resetMap() {
        mMap.clear();
        listLocsToDraw.clear();
        userMarkerCount = 0;
        hashMapUserMarker.clear();
    }

    private void addUserMarker(LatLng latLng) {
        listLocsToDraw.add(latLng);
        // create a marker for starting location
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);

        tvCustomMarkerOriginDest.setText(new Integer(++userMarkerCount).toString());
        options.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), customMarkerOriginDestRoot)));

        Marker marker = mMap.addMarker(options);
        hashMapUserMarker.put(userMarkerCount, marker);
        saveMarkerTag(marker, userMarkerCount);
        listMarkersToSave.add(marker);
    }

    protected void addUserMarker(LatLng latLng, InfoWindowData infoWindowData) {
        listLocsToDraw.add(latLng);
        // create a marker for starting location
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);

        tvCustomMarkerOriginDest.setText(new Integer(infoWindowData.getOrder()).toString());
        options.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), customMarkerOriginDestRoot)));

        Marker marker = mMap.addMarker(options);
        hashMapUserMarker.put(infoWindowData.getOrder(), marker);

        saveMarkerTag(marker, infoWindowData.getOrder());
    }

    private void addPlaceMarker(PlaceData placeData) {
        MarkerOptions options = new MarkerOptions();
        LatLng position = new LatLng(Double.parseDouble(placeData.getLat()),
                                     Double.parseDouble(placeData.getLng()));
        options.position(position);
        options.title(placeData.getName());
        options.snippet(placeData.getType());

        Marker marker = mMap.addMarker(options);
        listLocsOfPlaces.add(position);
        hashMapPlaceMarker.put(placeMarkerCount++, marker);
    }

    private void saveMarkerTag(Marker marker, int markerCount) {
        InfoWindowData infoWindowData = new InfoWindowData();
        infoWindowData.setTitle(marker.getTitle());
        infoWindowData.setSnippet(marker.getSnippet());
        infoWindowData.setOrder(markerCount);
        // it will be replaced to real score
        infoWindowData.setScore(Integer.toString(markerCount));
        // it will be replaced to real placeID
        infoWindowData.setPlaceID(Integer.toString(markerCount));

        marker.setTag(infoWindowData);
    }

    private void removeUserMarker(Marker marker) {
        listLocsToDraw.clear();
        listMarkersToSave.clear();
        int newMarkerCount = 0;
        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
        int removeIndex = infoWindowData.getOrder();
        marker.remove();
        hashMapUserMarker.remove(removeIndex);
        for (int i = 0; i <= userMarkerCount; ++i) {
            if (hashMapUserMarker.containsKey(i)) {
                Marker currentMarker = hashMapUserMarker.remove(i);
                LatLng currentPosition = currentMarker.getPosition();
                listLocsToDraw.add(currentPosition);
                newMarkerCount++;
            } else
                continue;
        }
        mMap.clear();
        hashMapUserMarker.clear();
        userMarkerCount = newMarkerCount;
        for (int i = 0; i < listLocsToDraw.size(); ++i) {
            // create a marker for starting location
            MarkerOptions options = new MarkerOptions();
            options.position(listLocsToDraw.get(i));
            if (i == 0 || i == listLocsToDraw.size() - 1) {
                tvCustomMarkerOriginDest.setText(new Integer(i + 1).toString());
                options.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), customMarkerOriginDestRoot)));
            } else {
                tvCustomMarkerWayPoint.setText(new Integer(i + 1).toString());
                options.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), customMarkerWayPointRoot)));
            }
            Marker currentMarker = mMap.addMarker(options);
            hashMapUserMarker.put(i + 1, currentMarker);
            saveMarkerTag(currentMarker, i+1);
            listMarkersToSave.add(currentMarker);
        }
        redrawRoute();
    }

    private void removePlaceMarker() {
        for(int i = 0; i < placeMarkerCount; ++i) {
            Marker marker = hashMapPlaceMarker.remove(i);
            marker.remove();
        }
        placeMarkerCount = 0;
        listLocsOfPlaces.clear();
    }
    /**
     * Convert View to Bitmap
     */
    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //view.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);

        Canvas canvas = new Canvas(bitmap);
        //Canvas canvas = new Canvas(smallBitmap);
        view.draw(canvas);

        return bitmap;
        //return smallBitmap;
        /**
         * ---> Deprecated Codes
         *
         * BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.map_marker_icon_red);
         * Bitmap bitmap = bitmapDrawable.getBitmap();
         * Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
         * return smallMarker;
         */
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PermissionCodes.REQUEST_CODE_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                PermissionCodes.getPermission(getContext(), getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION, PermissionCodes.REQUEST_CODE_FINE_LOCATION);
                PermissionCodes.getPermission(getContext(), getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION, PermissionCodes.REQUEST_CODE_COARSE_LOCATION);
            }
        } catch (SecurityException e) {
            Log.e("Error-Exception", e.getMessage());
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation == null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        MapUtility.DEFAULT_LOCATION, MapUtility.ZOOM_LEVEL));
                            } else {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), MapUtility.ZOOM_LEVEL));
                                Toast.makeText(getContext(), getResources().getString(R.string.locUpdateMessage), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Current location is null. Using defaults.", Toast.LENGTH_LONG).show();
                            // default location is soongsil univ.
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(MapUtility.DEFAULT_LOCATION, MapUtility.ZOOM_LEVEL));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private Context getContext() {
        return this;
    }

    private Activity getActivity() {
        return this;
    }

    /**
     * Classes & methods for drawing a route
     * DO NOT CHANGE
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

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jsonObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jsonObject);
            } catch (Exception e) {
                Log.d("DEBUG-Error", e.toString());
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;

            Log.d(".java", "result.size = " + result.size());

            for (int i = 0; i < result.size(); ++i) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); ++j) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                /**
                 * Route line options
                 */
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);
            }
            /**
             * This is the case of cannot drawing a route
             */
            if (result.size() == 0) {
                Toast.makeText(getContext(), "Invalid travel routes", Toast.LENGTH_LONG).show();
            } else {
                mMap.addPolyline(lineOptions);
            }
        }
    }

    /**
     * This method requests a code for draw routes between origin & dest
     *
     * @param origin
     * @param dest
     * @return url
     */
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String originString = "origin=" + origin.latitude + "," + origin.longitude;
        String destString = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";
        /**
         * DO NOT THINK ABOUT MODIFYING THIS CODE
         * mode : driving / walking / bicycling / transit
         * In korea, it only works if mode is transit
         */
        String mode = "mode=transit";

        String parameters = originString + "&" + destString + "&" + sensor + "&" + mode;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream is = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            is = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

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
            is.close();
            urlConnection.disconnect();
        }
        return data;
    }
}