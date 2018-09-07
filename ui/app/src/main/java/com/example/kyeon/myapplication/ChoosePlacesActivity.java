package com.example.kyeon.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author archslaveCW
 */

public class ChoosePlacesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isFirstPlaceAdded = false;
    private Marker lastMarker;
    // Received intent data
    private IntentData intentData;
    // Locations to draw
    private ArrayList<LatLng> listLocsToDraw = new ArrayList<>();
    private ArrayList<LatLng> listLocsOfPlaces = new ArrayList<>();
    private ArrayList<Marker> listMarkersToSave = new ArrayList<>();
    private HashMap<Integer, Marker> hashMapUserMarker = new HashMap<>();
    private HashMap<Integer, Marker> hashMapPlaceMarker = new HashMap<>();
    private int userMarkerCount = 0;
    private int placeMarkerCount = 0;
    private View customMarkerOriginDestRoot;
    private TextView tvCustomMarkerOriginDest;
    private View customMarkerWayPointRoot;
    private TextView tvCustomMarkerWayPoint;

    private static final int markerHeight = 0;
    private static final int bottomOffset = 18;

    private static final float DEFAULT_LEN = 2.5f;
    private static final int DEFAULT_LIM = 30;

    private MapWrapperLayout wrapperLayout;
    private ViewGroup userInfoWindow;
    private TextView userInfoTitle;
    private TextView userInfoSnippet;
    private ImageButton userInfoButton;
    private InfoWindowTouchListener infoWindowDeleteListener;

    private ViewGroup placeInfoWindow;
    private TextView placeInfoTitle;
    private TextView placeInfoSnippet;
    private ImageButton placeInfoButton;
    private InfoWindowTouchListener infoWindowAddListener;

    private ViewGroup firstPlaceInfoWindow;
    private TextView firstPlaceInfoTitle;
    private TextView firstPlaceInfoSnippet;

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
        Button scanButton = (Button)findViewById(R.id.scanButton);

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetMap();
            }
        });

        scanButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                placesUpdate(DEFAULT_LEN, DEFAULT_LIM);
            }
        });

        selectButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapUtility.saveMapUserMarkers(getContext(), mMap, listMarkersToSave, intentData.getTitle(), Integer.parseInt(intentData.getCurrentDay()));
            }
        });

        /**
         * Below codes are test code
         */
/**
 Button saveButton = (Button)findViewById(R.id.testSaveButton);
 saveButton.setOnClickListener(new Button.OnClickListener() {
 public void onClick(View view) {
 MapUtility.saveMapUserMarkers(getContext(), mMap, listMarkersToSave, intentData.getTitle(), 1);
 }
 });
 Button loadButton = (Button)findViewById(R.id.testLoadButton);
 loadButton.setOnClickListener(new Button.OnClickListener() {
 public void onClick(View view) {
 listMarkersToSave.clear();
 ArrayList<InfoWindowData> arrayList = MapUtility.loadMapUserMarkers(getContext(), intentData.getTitle() + "1.dat");
 for(InfoWindowData info : arrayList) {
 addUserMarker(info);
 }
 userMarkerCount = arrayList.size();
 }
 });
 */
        // test code end
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
        if (intentData.getPlaceType() == null)
            intentData.setPlaceType(getResources().getString(R.string.custom_place));
    }

    private void determineTrip() {
        if(isTravelEnded()) {
            saveIntentDatas();

        } else {
            saveIntentDatas();

        }
    }

    private void saveIntentDatas() {
        if(lastMarker != null) {
            IntentData newIntentData = new IntentData(getIntent());
            newIntentData.setCurrentDay(String.valueOf(Integer.parseInt(newIntentData.getCurrentDay()) + 1));
            newIntentData.setFirstPlace(lastMarker.getTitle());
            newIntentData.setPlaceLat(String.valueOf(lastMarker.getPosition().latitude));
            newIntentData.setPlaceLng(String.valueOf(lastMarker.getPosition().longitude));
            newIntentData.calcLatLng();
            newIntentData.setPlaceType(lastMarker.getSnippet());

            Intent intent = new Intent();
            newIntentData.transferDataToIntent(intent);
        }
    }

    private boolean isTravelEnded() {
        int currentDay = Integer.parseInt(intentData.getCurrentDay());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String d_yy = intentData.getdYY();
        String d_mm = intentData.getdMM();
        String d_dd = intentData.getdDD();
        String a_yy = intentData.getaYY();
        String a_mm = intentData.getaMM();
        String a_dd = intentData.getaDD();
        Date beginDate;
        Date endDate;
        try {
            beginDate = formatter.parse(d_yy + d_mm + d_dd);
            endDate = formatter.parse(a_yy + a_mm + a_dd);
            long diff = endDate.getTime() - beginDate.getTime();
            long diff_days = diff / (24 * 60 * 60 * 1000);

            if (currentDay > diff_days + 1)
                return true;
            else
                return false;

        } catch (Exception e) {
            Log.d("Error-Exception", e.getMessage());
        }
        return false;
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

        userInfoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.window_user_marker, null);
        userInfoTitle = (TextView) userInfoWindow.findViewById(R.id.tvTitle);
        userInfoSnippet = (TextView) userInfoWindow.findViewById(R.id.tvSnippet);
        userInfoButton = (ImageButton) userInfoWindow.findViewById(R.id.ibDelete);

        infoWindowDeleteListener = new InfoWindowTouchListener(userInfoButton) {
            @Override
            protected void onClickConfirmed(View v, final Marker marker) {
                //Toast.makeText(getContext(), "테스트 결과 이상 없음, 잘 눌림", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle(getResources().getString(R.string.remove_marker_title))
                        .setMessage(getResources().getString(R.string.remove_marker_description))
                        .setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.dialog_ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        PlaceData placeData = new PlaceData(marker.getTitle(), marker.getSnippet(), marker.getPosition());
                                        addPlaceMarker(placeData);
                                        removeUserMarker(marker);
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.dialog_no),
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
        userInfoButton.setOnTouchListener(infoWindowDeleteListener);

        placeInfoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.window_place_marker, null);
        placeInfoTitle = (TextView) placeInfoWindow.findViewById(R.id.tvTitle);
        placeInfoSnippet = (TextView) placeInfoWindow.findViewById(R.id.tvSnippet);
        placeInfoButton = (ImageButton) placeInfoWindow.findViewById(R.id.ibAdd);

        infoWindowAddListener = new InfoWindowTouchListener(placeInfoButton) {
            @Override
            protected void onClickConfirmed(View v, final Marker marker) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle(getResources().getString(R.string.add_marker_title))
                        .setMessage(getResources().getString(R.string.add_marker_description))
                        .setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.dialog_ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
                                        infoWindowData.setOrder(userMarkerCount);
                                        infoWindowData.setWindowType(InfoWindowData.TYPE_USER);
                                        addUserMarker(infoWindowData);
                                        removePlaceMarker(marker);
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.dialog_no),
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
        placeInfoButton.setOnTouchListener(infoWindowAddListener);

        firstPlaceInfoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.window_first_place_marker, null);
        firstPlaceInfoTitle = (TextView) firstPlaceInfoWindow.findViewById(R.id.tvTitle);
        firstPlaceInfoSnippet = (TextView) firstPlaceInfoWindow.findViewById(R.id.tvSnippet);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public View getInfoContents(Marker marker) {
                InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
                if (infoWindowData.getWindowType() == InfoWindowData.TYPE_USER) {
                    // Setting up the userInfoWindow with current's marker info
                    userInfoTitle.setText(marker.getTitle());
                    userInfoSnippet.setText(marker.getSnippet());
                    infoWindowDeleteListener.setMarker(marker);
                    // We must call this to set the current marker and userInfoWindow references
                    // to the MapWrapperLayout
                    wrapperLayout.setMarkerWithInfoWindow(marker, userInfoWindow);
                    return userInfoWindow;
                } else if (infoWindowData.getWindowType() == InfoWindowData.TYPE_PLACE) {
                    // Setting up the userInfoWindow with current's marker info
                    placeInfoTitle.setText(marker.getTitle());
                    placeInfoSnippet.setText(marker.getSnippet());
                    infoWindowAddListener.setMarker(marker);
                    // We must call this to set the current marker and userInfoWindow references
                    // to the MapWrapperLayout
                    wrapperLayout.setMarkerWithInfoWindow(marker, placeInfoWindow);
                    return placeInfoWindow;
                } else if (infoWindowData.getWindowType() == InfoWindowData.TYPE_FIRST_PLACE) {
                    firstPlaceInfoTitle.setText(intentData.getFirstPlace());
                    firstPlaceInfoSnippet.setText(intentData.getPlaceType());
                    wrapperLayout.setMarkerWithInfoWindow(marker, firstPlaceInfoWindow);
                }
                return null;
            }
        });

        /**
         * map click listener
         */
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                showCustomSelectDialog(latLng);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (marker.isInfoWindowShown())
                    marker.showInfoWindow();
                return false;
            }
        });

        addFirstPlaceMarker();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(intentData.getPlaceLatLng()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(MapUtility.ZOOM_LEVEL));

        PermissionCodes.getPermission(getContext(), getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION, PermissionCodes.REQUEST_CODE_COARSE_LOCATION);
    }

    private void placesUpdate(float len, int lim) {
        removeAllPlaceMarker();
        CameraPosition cameraPosition = mMap.getCameraPosition();
        /**
         * to do list
         * - request adjacency places to server
         */
        String currentLat = new Double(cameraPosition.target.latitude).toString();
        String currentLng = new Double(cameraPosition.target.longitude).toString();
        if(len == 0) {
            len = 2.5f;
        }
        if(lim == 0) {
            lim = 30;
        }
        MapUtility.FindPlacesTask findPlacesTask = new MapUtility.FindPlacesTask(currentLat, currentLng, len, lim, false);
        findPlacesTask.execute();
        try {
            adjacencyPlaces = findPlacesTask.get();
            if(adjacencyPlaces != null) {
                ArrayList<PlaceData> placeDataArrayList = MapUtility.placeParsing(adjacencyPlaces);
                if(placeDataArrayList != null) {
                    for(PlaceData placeData : placeDataArrayList) {
                        addPlaceMarker(placeData);
                    }
                }
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.scan_fail_message), Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

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
        listMarkersToSave.clear();
        isFirstPlaceAdded = false;
        addFirstPlaceMarker();
    }

    private void showCustomSelectDialog(final LatLng latLng) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(getResources().getString(R.string.custom_select_title))
                .setMessage(getResources().getString(R.string.custom_select_description))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.dialog_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addUserMarker(latLng);
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.dialog_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    /**
     * Deprecated method
     * --> Just Test code for map click
     * --> In release, below codes are forbidden
     */
    @Deprecated
    private void addUserMarker(LatLng latLng) {
        listLocsToDraw.add(latLng);
        // create a marker for starting location
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);

        tvCustomMarkerOriginDest.setText(new Integer(++userMarkerCount).toString());
        options.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), customMarkerOriginDestRoot)));

        Marker marker = mMap.addMarker(options);
        hashMapUserMarker.put(userMarkerCount, marker);
        saveMarkerTag(marker, userMarkerCount, InfoWindowData.TYPE_USER);
        listMarkersToSave.add(marker);
        drawRoute();
        lastMarker = marker;
    }

    protected void addUserMarker(InfoWindowData infoWindowData) {
        listLocsToDraw.add(infoWindowData.getLatLng());
        // create a marker for starting location
        MarkerOptions options = new MarkerOptions();
        options.position(infoWindowData.getLatLng());

        tvCustomMarkerOriginDest.setText(new Integer(++userMarkerCount).toString());
        options.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), customMarkerOriginDestRoot)));

        Marker marker = mMap.addMarker(options);
        loadMarkerTag(marker, infoWindowData);
        hashMapUserMarker.put(userMarkerCount, marker);

        saveMarkerTag(marker, userMarkerCount, InfoWindowData.TYPE_USER);
        listMarkersToSave.add(marker);
        drawRoute();
        lastMarker = marker;
    }

    private void addFirstPlaceMarker() {
        if (isFirstPlaceAdded == false) {
            listLocsToDraw.add(intentData.getPlaceLatLng());
            // create a marker for starting location
            MarkerOptions options = new MarkerOptions();
            options.position(intentData.getPlaceLatLng());

            tvCustomMarkerOriginDest.setText(new Integer(++userMarkerCount).toString());
            options.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), customMarkerOriginDestRoot)));

            Marker marker = mMap.addMarker(options);
            marker.setSnippet(intentData.getPlaceType());
            marker.setTitle(intentData.getFirstPlace());
            hashMapUserMarker.put(userMarkerCount, marker);
            saveMarkerTag(marker, userMarkerCount, InfoWindowData.TYPE_FIRST_PLACE);
            listMarkersToSave.add(marker);
            isFirstPlaceAdded = true;
        }
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
        hashMapPlaceMarker.put(userMarkerCount++, marker);
        saveMarkerTag(marker, userMarkerCount, InfoWindowData.TYPE_PLACE);
    }

    private void saveMarkerTag(Marker marker, int markerCount, int windowType) {
        InfoWindowData infoWindowData = new InfoWindowData();
        infoWindowData.setTitle(marker.getTitle());
        infoWindowData.setSnippet(marker.getSnippet());
        infoWindowData.setOrder(markerCount);
        // it will be replaced to real score
        infoWindowData.setScore(Integer.toString(markerCount));
        // it will be replaced to real placeID
        infoWindowData.setPlaceID(Integer.toString(markerCount));
        infoWindowData.setLatLng(marker.getPosition());
        infoWindowData.setWindowType(windowType);

        marker.setTag(infoWindowData);
    }

    private void loadMarkerTag(Marker marker, InfoWindowData infoWindowData) {
        marker.setTitle(infoWindowData.getTitle());
        marker.setSnippet(infoWindowData.getSnippet());
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
            saveMarkerTag(currentMarker, i + 1, InfoWindowData.TYPE_USER);
            listMarkersToSave.add(currentMarker);
        }
        redrawRoute();
    }

    private void removeAllPlaceMarker() {
        for (int i = 0; i < placeMarkerCount; ++i) {
            Marker marker = hashMapPlaceMarker.remove(i);
            marker.remove();
        }
        placeMarkerCount = 0;
        listLocsOfPlaces.clear();
    }

    private void removePlaceMarker(Marker marker) {
        int newMarkerCount = 0;
        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
        int removeIndex = infoWindowData.getOrder();
        marker.remove();
        HashMap<Integer, Marker> newHashMap = new HashMap<>();
        hashMapPlaceMarker.remove(removeIndex);
        for (int i = 0; i <= placeMarkerCount; ++i) {
            if (hashMapPlaceMarker.containsKey(i)) {
                Marker currentMarker = hashMapPlaceMarker.remove(i);
                newHashMap.put(newMarkerCount++, currentMarker);
            } else
                continue;
        }
        hashMapPlaceMarker = newHashMap;
        placeMarkerCount = newMarkerCount;
    }

    /**
     * Convert View to Bitmap
     */
    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
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