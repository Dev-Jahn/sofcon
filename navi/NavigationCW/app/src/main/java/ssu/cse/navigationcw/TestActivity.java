package ssu.cse.navigationcw;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class TestActivity  extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> listLocsToDraw;

    //private List<GroundOverlay> mMapOverlay;
    //private Projection mProjection;

//    private LinearLayout mLinearLayout;
//    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        listLocsToDraw = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng startLatLng = new LatLng(37.525007, 126.971547);
        LatLng destLatLng = new LatLng(37.625007, 127.071547);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        listLocsToDraw.add(startLatLng);
        listLocsToDraw.add(destLatLng);

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
