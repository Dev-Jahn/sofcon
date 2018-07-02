package ssu.cse.navigationcw;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQUEST_CODE_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MarkerOptions markerOptions1 = new MarkerOptions();
        MarkerOptions markerOptions2 = new MarkerOptions();
        // Add a marker in Sydney and move the camera
        LatLng startLatLng = new LatLng(37.525007, 126.971547);
        LatLng destLatLng = new LatLng(37.625007, 127.071547);

        markerOptions1.position(startLatLng);
        markerOptions2.position(destLatLng);
        markerOptions1.title("출발지");
        markerOptions2.title("도착지");

        mMap.addMarker(markerOptions1);
        mMap.addMarker(markerOptions2);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
}
