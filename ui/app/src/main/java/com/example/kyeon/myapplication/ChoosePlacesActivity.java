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
import android.location.Address;
import android.location.Geocoder;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * @author archslaveCW
 */

public class ChoosePlacesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isFirstPlaceAdded = false;
    private Marker lastUserMarker;
    // Received intent data
    private IntentData intentData;
    // Locations to draw
    private ArrayList<LatLng> listLocsToDraw = new ArrayList<>();
    private ArrayList<LatLng> listLocsOfPlaces = new ArrayList<>();
    private ArrayList<Marker> listMarkersToSave = new ArrayList<>();
    private ArrayList<OptimizationData> listLocsToOptimize = new ArrayList<>();
    private ArrayList<InfoWindowData> listDatasToLoad;

    private HashMap<Integer, Marker> hashMapUserMarker = new HashMap<>();
    private HashMap<Integer, Marker> hashMapPlaceMarker = new HashMap<>();
    private String[] saved_info;
    private String UID;
    private int userMarkerCount = 0;
    private int placeMarkerCount = 0;
    private View customMarkerOriginDestRoot;
    private TextView tvCustomMarkerOriginDest;
    private View customMarkerWayPointRoot;
    private TextView tvCustomMarkerWayPoint;

    private static final int markerHeight = 0;
    private static final int bottomOffset = 18;

    private static final float DEFAULT_LEN = 2.5f;
    private static final String DEFAULT_LIM = "30";

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
    private String placeBitmapFilePath;

    private Intent returnIntent;

    //for save travel
    Travel travel;

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
        Button scanButton = (Button) findViewById(R.id.scanButton);

        if (intentData.isReloaded()) {
            listDatasToLoad = MapUtility.loadMapUserMarkers(getContext(), intentData.getTitle(), intentData.getCurrentDay());
            ViewGroup layout = (ViewGroup) cancelButton.getParent();
            if (layout != null) {
                layout.removeView(cancelButton);
                layout.removeView(selectButton);
                layout.removeView(scanButton);
            }
        } else {


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
                    determineTrip();
                }
            });
        }
        // test code end
        customMarkerOriginDestRoot = LayoutInflater.from(this).inflate(R.layout.marker_custom_origin_dest, null);
        tvCustomMarkerOriginDest = (TextView) customMarkerOriginDestRoot.findViewById(R.id.custom_marker_text);
        customMarkerWayPointRoot = LayoutInflater.from(this).inflate(R.layout.marker_custom_waypoint, null);
        tvCustomMarkerWayPoint = (TextView) customMarkerWayPointRoot.findViewById(R.id.custom_marker_text);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getIntentDatas() {
        Intent intent = getIntent();
        intentData = new IntentData(intent);
        if (intentData.getPlaceType() == null)
            intentData.setPlaceType(getResources().getString(R.string.custom_place));
    }

    private void determineTrip() {
        MapUtility.saveMapUserMarkers(getContext(), listMarkersToSave, intentData.getTitle(), intentData.getCurrentDay());
        addPlaceDatas();
        hideAllInfoWindows();
        captureScreenAndSaveAndFinish();

    }

    private void saveIntentDatas() {
        if (lastUserMarker != null) {
            IntentData newIntentData = new IntentData(getIntent());
            newIntentData.setCurrentDay(String.valueOf(Integer.parseInt(newIntentData.getCurrentDay()) + 1));
            newIntentData.setFirstPlace(lastUserMarker.getTitle());
            newIntentData.setPlaceLat(String.valueOf(lastUserMarker.getPosition().latitude));
            newIntentData.setPlaceLng(String.valueOf(lastUserMarker.getPosition().longitude));
            newIntentData.calcLatLng();
            newIntentData.setPlaceType(lastUserMarker.getSnippet());
            newIntentData.setPlaceBitmapFilePath(placeBitmapFilePath);

            returnIntent = new Intent();
            returnIntent.putExtra("travelData", travel);
            Log.d("travelTest", "beforereturn: " + travel.dailyDiary[0].review.get(0).place_name);
            newIntentData.transferDataToIntent(returnIntent);
        }
    }

    private void addPlaceDatas() {
        int index = getIntent().getIntExtra("currentDay", 0);
        Bundle b = getIntent().getExtras();
        travel = (Travel) getIntent().getExtras().getSerializable("travelData");
        for (int i = 0; i < listMarkersToSave.size(); i++) {
            Marker saveMarker = listMarkersToSave.get(i);
            Log.d("placeIdtest", ((InfoWindowData)saveMarker.getTag()).getPlaceID());
            travel.dailyDiary[index - 1].addPlace(((InfoWindowData)saveMarker.getTag()).getPlaceID(), saveMarker.getTitle(), saveMarker.getSnippet());
        }
    }

    private void captureScreenAndSaveAndFinish() {
        Log.d("DEBUG-TEST", "snapshot is called");
        final GoogleMap.SnapshotReadyCallback snapshotReadyCallback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                removeAllPlaceMarker();
                hideAllInfoWindows();
                String filePath = getContext().getFilesDir().getPath().toString() + "/"
                        + intentData.getTitle() + intentData.getCurrentDay() + ".png";
                File file = new File(filePath);
                Log.d("DEBUG-TEST", "스냅샷 시작 in ChoosePlacesActivity");
                try {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    snapshot.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.close();
                    Log.d("DEBUG-TEST", file.getAbsolutePath() + " in ChoosePlacesActivity");
                    Log.d("DEBUG-TEST", "스냅샷 완료 in ChoosePlacesActivity");
                } catch (IOException e) {
                    Log.d("DEBUG-TEST", "스냅샷 에러 in ChoosePlacesActivity");
                    Log.d("DEBUG-TEST", e.getMessage());
                }
                placeBitmapFilePath = filePath;
                saveIntentDatas();
                captureScreenForNextRoutes();
            }
        };
        mMap.snapshot(snapshotReadyCallback);
    }

    private void captureScreenForNextRoutes() {
        Log.d("DEBUG-TEST", "snapshot is called");
        final GoogleMap.SnapshotReadyCallback snapshotReadyCallback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                InfoWindowData infoWindowData = new InfoWindowData(lastUserMarker);
                removeAllPlaceMarker();
                resetMap();
                addUserMarker(infoWindowData, true);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(infoWindowData.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(MapUtility.ZOOM_LEVEL));

                String filePath = getContext().getFilesDir().getPath().toString() + "/"
                        + intentData.getTitle() + (intentData.getCurrentDay() + 1) + ".png";
                File file = new File(filePath);
                Log.d("DEBUG-TEST", "스냅샷 시작 in ChoosePlacesActivity");
                try {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    snapshot.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.close();
                    Log.d("DEBUG-TEST", file.getAbsolutePath() + " in ChoosePlacesActivity");
                    Log.d("DEBUG-TEST", "스냅샷 완료 in ChoosePlacesActivity");
                } catch (IOException e) {
                    Log.d("DEBUG-TEST", "스냅샷 에러 in ChoosePlacesActivity");
                    Log.d("DEBUG-TEST", e.getMessage());
                }
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        };
        mMap.snapshot(snapshotReadyCallback);
    }

    private void hideAllInfoWindows() {
        for (Marker marker : listMarkersToSave)
            marker.hideInfoWindow();
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
                                        PlaceData placeData = new PlaceData(marker.getTitle(), marker.getSnippet(), marker.getPosition(), ((InfoWindowData) marker.getTag()).getPlaceID());
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
                                        addUserMarker(infoWindowData, false);
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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
            }
        });

        PermissionCodes.getPermission(getContext(), getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION, PermissionCodes.REQUEST_CODE_COARSE_LOCATION);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(intentData.getPlaceLatLng()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(MapUtility.ZOOM_LEVEL));

        if (intentData.isReloaded()) {
            placeInfoWindow.removeView(placeInfoButton);
            userInfoWindow.removeView(userInfoButton);
            for (InfoWindowData infoWindowData : listDatasToLoad) {
                addUserMarker(infoWindowData, true);
            }
        } else {
            /**
             * map click listener
             */
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    showCustomSelectDialog(latLng);
                }
            });

            addFirstPlaceMarker();

            if(intentData.isAuto()) {
                String dirPath = getFilesDir().getAbsolutePath();
                File file  =  new File(dirPath);
                String content ="", temp="";

                if(file.listFiles().length>0) {
                    for (File f : file.listFiles()) {
                        String f_name = f.getName();
                        String loadPath = dirPath + File.separator + f_name;
                        if (f_name.equals("account_setup.txt")) {
                            try {
                                FileInputStream fis = new FileInputStream(loadPath);
                                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));

                                while ((temp = bufferReader.readLine()) != null) content += temp;
                                break;
                            } catch (Exception e) {
                            }
                        }
                    }
                    saved_info = content.split(" ");
                }
                UID = saved_info[0];

                MapUtility.FindPlacesTask findPlacesTask = new MapUtility.FindPlacesTask(String.valueOf(listLocsToDraw.get(0).latitude), String.valueOf(listLocsToDraw.get(0).longitude),
                        10, "5", 1, "restaurants", UID);
                findPlacesTask.execute();
                try{
                    adjacencyPlaces = findPlacesTask.get();
                    Log.d("DEBUG-TEST", "!!!" +adjacencyPlaces);
                    if(adjacencyPlaces != null) {
                        ArrayList<PlaceData> placeDataArrayList = MapUtility.placeParsing(adjacencyPlaces);
                        if(placeDataArrayList != null) {
                            for(PlaceData placeData : placeDataArrayList) {
                                InfoWindowData infoWindowData = new InfoWindowData();
                                infoWindowData.setWindowType(InfoWindowData.TYPE_USER);
                                infoWindowData.setPlaceID(placeData.getPlaceId());
                                infoWindowData.setSnippet(placeData.getType());
                                infoWindowData.setTitle(placeData.getName());
                                LatLng latLng = new LatLng(Double.parseDouble(placeData.getLat()), Double.parseDouble(placeData.getLng()));
                                infoWindowData.setLatLng(latLng);
                                addUserMarker(infoWindowData, true);
                            }
                            optimizationRoutes();
                        }
                    }
                } catch(InterruptedException e) {

                } catch(ExecutionException e) {

                }
            }
        }
    }

    private void placesUpdate(float len, String lim) {
        removeAllPlaceMarker();
        CameraPosition cameraPosition = mMap.getCameraPosition();
        /**
         * to do list
         * - request adjacency places to server
         */
        String currentLat = new Double(cameraPosition.target.latitude).toString();
        String currentLng = new Double(cameraPosition.target.longitude).toString();
        if (len == 0) {
            len = DEFAULT_LEN;
        }
        if (lim == null) {
            lim = DEFAULT_LIM;
        }
        MapUtility.FindPlacesTask findPlacesTask = new MapUtility.FindPlacesTask(currentLat, currentLng, len, lim, 0);
        findPlacesTask.execute();
        try {
            adjacencyPlaces = findPlacesTask.get();
            if (adjacencyPlaces != null) {
                ArrayList<PlaceData> placeDataArrayList = MapUtility.placeParsing(adjacencyPlaces);
                if (placeDataArrayList != null) {
                    for (PlaceData placeData : placeDataArrayList) {
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
            DownloadTask downloadTask = new DownloadTask(origin, dest, false);
            downloadTask.execute(url);
        }
    }

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
                DownloadTask downloadTask = new DownloadTask(origin, dest, true);
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
        listLocsToOptimize.clear();
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

    private void addUserMarker(LatLng latLng) {
        listLocsToDraw.add(latLng);
        // create a marker for starting location
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);

        tvCustomMarkerOriginDest.setText(new Integer(++userMarkerCount).toString());
        options.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), customMarkerOriginDestRoot)));

        Marker marker = mMap.addMarker(options);

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        marker.setSnippet(getResources().getString(R.string.default_place_name));
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 10);
            if (addressList == null || addressList.size() == 0) {
                marker.setTitle(getResources().getString(R.string.default_place_name));
            } else {
                Address address = addressList.get(0);
                marker.setTitle(address.getAddressLine(0).toString());
            }
        } catch (IOException e) {
            Log.d("DEBUG-EXCEPTION", e.getMessage());
            marker.setTitle(getResources().getString(R.string.default_place_name));
        }

        hashMapUserMarker.put(userMarkerCount, marker);
        saveMarkerTag(marker, userMarkerCount, InfoWindowData.TYPE_USER);
        listMarkersToSave.add(marker);
        lastUserMarker = marker;
        // drawRoute();
        optimizationRoutes();
    }

    protected void addUserMarker(InfoWindowData infoWindowData, boolean isOptimized) {
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
        lastUserMarker = marker;
        if (isOptimized) {
            drawRoute();
        } else {
            optimizationRoutes();
        }
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
        hashMapPlaceMarker.put(placeMarkerCount++, marker);
        saveMarkerTag(marker, placeMarkerCount, InfoWindowData.TYPE_PLACE, placeData.getPlaceId());
    }

    private void saveMarkerTag(Marker marker, int markerCount, int windowType) {
        InfoWindowData infoWindowData = new InfoWindowData();
        infoWindowData.setTitle(marker.getTitle());
        infoWindowData.setSnippet(marker.getSnippet());
        infoWindowData.setOrder(markerCount);
        // it will be replaced to real score
        infoWindowData.setScore(Integer.toString(markerCount));
        // it will be replaced to real placeID
        infoWindowData.setPlaceID("0");
        infoWindowData.setLatLng(marker.getPosition());
        infoWindowData.setWindowType(windowType);

        marker.setTag(infoWindowData);
    }

    private void saveMarkerTag(Marker marker, int markerCount, int windowType, String placeId) {
        InfoWindowData infoWindowData = new InfoWindowData();
        infoWindowData.setTitle(marker.getTitle());
        infoWindowData.setSnippet(marker.getSnippet());
        infoWindowData.setOrder(markerCount);
        // it will be replaced to real score
        infoWindowData.setScore(Integer.toString(markerCount));
        // it will be replaced to real placeID
        infoWindowData.setPlaceID(placeId);
        infoWindowData.setLatLng(marker.getPosition());
        infoWindowData.setWindowType(windowType);

        marker.setTag(infoWindowData);
    }

    private void loadMarkerTag(Marker marker, InfoWindowData infoWindowData) {
        marker.setTitle(infoWindowData.getTitle());
        marker.setSnippet(infoWindowData.getSnippet());
    }

    private void removeUserMarker(Marker marker) {
        ArrayList<InfoWindowData> infoWindowDataArrayList = new ArrayList<>();
        for (Marker saveMarker : listMarkersToSave) {
            if (saveMarker != marker)
                infoWindowDataArrayList.add(((InfoWindowData) saveMarker.getTag()));
        }
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
            loadMarkerTag(currentMarker, infoWindowDataArrayList.get(i));
            currentMarker.setTag(infoWindowDataArrayList.get(i));
            hashMapUserMarker.put(i + 1, currentMarker);
            if (i != 0) {
                saveMarkerTag(currentMarker, i + 1, InfoWindowData.TYPE_USER);
            } else {
                saveMarkerTag(currentMarker, i + 1, InfoWindowData.TYPE_FIRST_PLACE);
            }

            listMarkersToSave.add(currentMarker);

        }
        redrawRoute();
        // optimizationRoutes();
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

    private void reestablishUserMarker() {
        listLocsToDraw.clear();
        listMarkersToSave.clear();
        OptimizationData optimizationData;
        ArrayList<InfoWindowData> infoWindowDataArrayList = new ArrayList<>();

        boolean[] startCheck = new boolean[userMarkerCount + 1];
        int startVertex = 1;
        int preVertex = 0;
        for (int i = 0; i < userMarkerCount - 1; ++i) {
            OptimizationGraph graph = new OptimizationGraph(userMarkerCount);
            for (int j = 0; j < listLocsToOptimize.size(); ++j) {
                optimizationData = listLocsToOptimize.get(j);

                graph.input(optimizationData.getOriginIndex(), optimizationData.getDestIndex(), optimizationData.getDistance());
                for (int k = 1; k < userMarkerCount + 1; ++k) {
                    if (startCheck[k] == true) {
                        Log.d("DEBUG-TT", "k : " + k);
                        graph.delete(k);
                    }
                }
            }
            preVertex = startVertex;
            startCheck[startVertex] = true;
            if (startVertex == 0)
                break;
            startVertex = graph.dijkstra(startVertex);
            infoWindowDataArrayList.add(new InfoWindowData(hashMapUserMarker.get(preVertex)));
            Log.d("DEBUG-TEST", "수행되었음" + preVertex + "에서 " + startVertex);
        }

        for (int i = 1; i < userMarkerCount + 1; ++i) {
            if (startCheck[i] == false)
                infoWindowDataArrayList.add(new InfoWindowData(hashMapUserMarker.get(i)));
        }

        mMap.clear();
        hashMapUserMarker.clear();
        int originUserMarkerCount = userMarkerCount;
        userMarkerCount = 0;
        for (int i = 0; i < originUserMarkerCount; i++) {
            if (i == 0) {
                isFirstPlaceAdded = false;
                addFirstPlaceMarker();
            } else {
                addUserMarker(infoWindowDataArrayList.get(i), true);
            }
        }
        userMarkerCount = originUserMarkerCount;
        listLocsToOptimize.clear();
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

    private void optimizationRoutes() {
        if (listLocsToDraw.size() > 2) {
            for (int originIndex = 1; originIndex < userMarkerCount; ++originIndex) {
                Marker originMarker = hashMapUserMarker.get(originIndex);
                for (int destIndex = originIndex + 1; destIndex <= userMarkerCount; ++destIndex) {
                    Marker destMarker = hashMapUserMarker.get(destIndex);
                    String url = getDirectionsUrl(originMarker.getPosition(), destMarker.getPosition());
                    OptimizationData optimizationData = new OptimizationData(originMarker, destMarker);
                    OptimizationTask optimizationTask = new OptimizationTask(optimizationData);
                    optimizationTask.execute(url);
                }
            }
        } else {
            redrawRoute();
        }
    }

    private class OptimizationTask extends AsyncTask<String, Void, String> {
        private OptimizationData optimizationData;

        public OptimizationTask(OptimizationData optimizationData) {
            this.optimizationData = optimizationData;
        }

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

            CalcRouteDistanceTask calcRouteDistanceTask = new CalcRouteDistanceTask(optimizationData);
            calcRouteDistanceTask.execute(result);
        }
    }

    private class CalcRouteDistanceTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        private OptimizationData optimizationData;

        public CalcRouteDistanceTask(OptimizationData optimizationData) {
            this.optimizationData = optimizationData;
        }

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
            ArrayList<LatLng> points = new ArrayList();
            double totalDistance = 0;
            for (int i = 0; i < result.size(); ++i) {
                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); ++j) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
            }
            if (result.size() == 0) {
                points.add(optimizationData.getOriginMarker().getPosition());
                points.add(optimizationData.getDestMarker().getPosition());
            }
            for (int originIndex = 0; originIndex < points.size() - 1; ++originIndex) {
                int destIndex = originIndex + 1;
                LatLng originLatLng = points.get(originIndex);
                LatLng destLatLng = points.get(destIndex);

                totalDistance += ((originLatLng.latitude - destLatLng.latitude) * (originLatLng.latitude - destLatLng.latitude)
                        + (originLatLng.longitude - destLatLng.longitude) * (originLatLng.longitude - destLatLng.longitude));
            }
            optimizationData.setDistance(totalDistance);
            listLocsToOptimize.add(optimizationData);

            if (listLocsToOptimize.size() == ((userMarkerCount - 1) * userMarkerCount) / 2) {
                Collections.sort(listLocsToOptimize);
                for (OptimizationData optimizationData : listLocsToOptimize) {
                    Log.d("DEBUG-TABLE", optimizationData.getOriginIndex() + " to " + optimizationData.getDestIndex() + " : " + optimizationData.getDistance());
                }
                reestablishUserMarker();
            }
        }
    }


    /**
     * Classes & methods for drawing a route
     * DO NOT CHANGE
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        private LatLng origin;
        private LatLng dest;
        private boolean redraw;

        public DownloadTask(LatLng origin, LatLng dest, boolean redraw) {
            this.origin = origin;
            this.dest = dest;
            this.redraw = redraw;
        }

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

            ParserTask parserTask = new ParserTask(origin, dest, redraw);
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        private LatLng origin;
        private LatLng dest;
        private boolean redraw;

        public ParserTask(LatLng origin, LatLng dest, boolean redraw) {
            this.origin = origin;
            this.dest = dest;
            this.redraw = redraw;
        }

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
            PolylineOptions routeLineOptions = null;

            for (int i = 0; i < result.size(); ++i) {
                points = new ArrayList();
                routeLineOptions = new PolylineOptions();

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
                routeLineOptions.addAll(points);
                routeLineOptions.width(12);
                routeLineOptions.color(Color.RED);
                routeLineOptions.geodesic(true);
            }
            /**
             * This is the case of cannot drawing a route
             */
            if (result.size() == 0) {
                Log.d("DEBUG-TEST", "result size : " + result.size());
                if (redraw == false) {
                    PolylineOptions justLineOptions = new PolylineOptions();
                    justLineOptions = new PolylineOptions();
                    justLineOptions.add(origin);
                    justLineOptions.add(dest);
                    justLineOptions.width(12);
                    justLineOptions.color(Color.BLUE);
                    justLineOptions.geodesic(true);
                    mMap.addPolyline(justLineOptions);
                } else {
                    PolylineOptions justLineOptions = new PolylineOptions();
                    justLineOptions = new PolylineOptions();
                    justLineOptions.add(origin);
                    justLineOptions.add(dest);
                    justLineOptions.width(12);
                    justLineOptions.color(Color.BLUE);
                    justLineOptions.geodesic(true);
                    mMap.addPolyline(justLineOptions);
                }
            } else {
                mMap.addPolyline(routeLineOptions);
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


    private Context getContext() {
        return this;
    }

    private Activity getActivity() {
        return this;
    }
}