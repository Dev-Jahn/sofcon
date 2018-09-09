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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * @author archslaveCW
 */

public class ChooseFirstPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private IntentData intentData;
    private Marker selectedMarker;
    private Intent returnIntent;

    private ArrayList<LatLng> listLocsOfPlaces = new ArrayList<>();
    private HashMap<Integer, Marker> hashMapPlaceMarker = new HashMap<>();
    private static int placeMarkerCount = 0;

    private static final float DEFAULT_LEN = 2.5f;
    private static final int DEFAULT_LIM = 30;

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private String adjacencyPlaces;

    private View customMarkerRoot;
    private static final int markerHeight = 0;
    private static final int bottomOffset = 18;

    private MapWrapperLayout wrapperLayout;
    private ViewGroup placeInfoWindow;
    private TextView placeInfoTitle;
    private TextView placeInfoSnippet;
    private ImageButton placeInfoButton;
    private InfoWindowTouchListener infoWindowAddListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_first_place);
        getIntentDatas();

        ImageButton closeImgButton = (ImageButton) findViewById(R.id.closeButton);

        closeImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ChooseFirstPlaceActivity.this.overridePendingTransition(R.anim.stay, R.anim.sliding_down);
            }
        });

        Button scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                placesUpdate(DEFAULT_LEN, DEFAULT_LIM);
            }
        });

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        customMarkerRoot = LayoutInflater.from(this).inflate(R.layout.marker_custom_origin_dest, null);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        hashMapPlaceMarker.clear();

        wrapperLayout = (MapWrapperLayout) findViewById(R.id.wrapperLayout);
        wrapperLayout.init(mMap, MapWrapperLayout.getPixelsFromDp(getContext(), markerHeight + bottomOffset));

        placeInfoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.window_place_marker, null);
        placeInfoTitle = (TextView) placeInfoWindow.findViewById(R.id.tvTitle);
        placeInfoSnippet = (TextView) placeInfoWindow.findViewById(R.id.tvSnippet);
        placeInfoButton = (ImageButton) placeInfoWindow.findViewById(R.id.ibAdd);

        infoWindowAddListener = new InfoWindowTouchListener(placeInfoButton) {
            @Override
            protected void onClickConfirmed(View v, final Marker marker) {
                showSelectDialog(marker);
            }
        };
        placeInfoButton.setOnTouchListener(infoWindowAddListener);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public View getInfoContents(Marker marker) {
                InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
                // Setting up the userInfoWindow with current's marker info
                placeInfoTitle.setText(marker.getTitle());
                placeInfoSnippet.setText(marker.getSnippet());
                infoWindowAddListener.setMarker(marker);
                // We must call this to set the current marker and userInfoWindow references
                // to the MapWrapperLayout
                wrapperLayout.setMarkerWithInfoWindow(marker, placeInfoWindow);
                return placeInfoWindow;
            }
        });

        /**
         * map click listener
         */
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addFirstPlaceMarker(latLng);
            }
        });

        mLocationPermissionGranted = PermissionCodes.getPermission(getContext(), getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION, PermissionCodes.REQUEST_CODE_FINE_LOCATION);
        PermissionCodes.getPermission(getContext(), getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION, PermissionCodes.REQUEST_CODE_COARSE_LOCATION);

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void captureScreenAndFinish() {
        Log.d("DEBUG-TEST", "snapshot is called");
        final GoogleMap.SnapshotReadyCallback snapshotReadyCallback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                String filePath = getContext().getFilesDir().getPath().toString() + "/tempImage1.png";
                File file = new File(filePath);
                if(file.exists()) {
                    file.delete();
                    file = new File(filePath);
                }
                Log.d("DEBUG-TEST", file.getAbsolutePath() + "in TripPlanActivity");
                try {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    snapshot.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.close();
                } catch(IOException e) {
                    Log.d("DEBUG-TEST", "파일 출력 에러 in ChooseFirstPlaceActivity");
                    Log.d("DEBUG-TEST", e.getMessage());
                }
                returnIntent.putExtra(MapUtility.PLACE_BITMAP_FILE_PATH_TAG, filePath);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        };
        mMap.snapshot(snapshotReadyCallback);
    }

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

    private void placesUpdate(float len, int lim) {
        removeAllPlaceMarker();
        CameraPosition cameraPosition = mMap.getCameraPosition();
        /**
         * to do list
         * - request adjacency places to server
         */
        String currentLat = new Double(cameraPosition.target.latitude).toString();
        String currentLng = new Double(cameraPosition.target.longitude).toString();
        if (len == 0) {
            len = 2.5f;
        }
        if (lim == 0) {
            lim = 30;
        }
        MapUtility.FindPlacesTask findPlacesTask = new MapUtility.FindPlacesTask(currentLat, currentLng, len, lim, false);
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

    private void removeAllPlaceMarker() {
        for (int i = 0; i < placeMarkerCount; ++i) {
            Marker marker = hashMapPlaceMarker.remove(i);
            if (marker != null)
                marker.remove();
        }
        placeMarkerCount = 0;
        listLocsOfPlaces.clear();
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
        saveMarkerTag(marker, placeMarkerCount, InfoWindowData.TYPE_PLACE);
    }

    private void addFirstPlaceMarker(LatLng latLng) {
        if(selectedMarker == null) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            MarkerOptions options = new MarkerOptions();
            options.position(latLng);
            options.snippet(getResources().getString(R.string.default_place_name));
            try {
                List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 10);
                if (addressList == null || addressList.size() == 0) {
                    options.title(getResources().getString(R.string.default_place_name));
                } else {
                    Address address = addressList.get(0);
                    options.title(address.getAddressLine(0).toString());
                }
            } catch (IOException e) {
                Log.d("DEBUG-EXCEPTION", e.getMessage());
                options.title(getResources().getString(R.string.default_place_name));
            }

            options.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), customMarkerRoot)));

            selectedMarker = mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedMarker.getPosition()));
            showSelectDialog(selectedMarker);
            // selectedMarker.remove();
            // showSelectDialog(latLng);
        } else {
            selectedMarker.remove();
            addFirstPlaceMarker(latLng);
        }
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

    private void showSelectDialog(final Marker marker) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(getResources().getString(R.string.set_place_title))
                .setMessage(getResources().getString(R.string.set_place_description))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.dialog_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                returnIntent = new Intent();
                                returnIntent.putExtra(MapUtility.PLACE_LAT_TAG, String.valueOf(marker.getPosition().latitude));
                                returnIntent.putExtra(MapUtility.PLACE_LNG_TAG, String.valueOf(marker.getPosition().longitude));
                                returnIntent.putExtra(MapUtility.PLACE_NAME_TAG, marker.getTitle());
                                returnIntent.putExtra(MapUtility.PLACE_TYPE_TAG, marker.getSnippet());
                                captureScreenAndFinish();
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
}
