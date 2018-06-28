package ssu.cse.navigationcw;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Request code for ACCESS_FINE_LOCATION
    private static final int REQUEST_CODE_LOCATION = 3125;
    private Button buttonMap;
    private Button buttonPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonMap = (Button)findViewById(R.id.buttonMap);
        buttonPlace = (Button)findViewById(R.id.buttonPlace);

        /**
         * Check Permissions
         * - ACCESS_FINE_LOCATION
         *
         * @ IF permission was not allowed,
         *   We should show some dialogs that why we needs this permissions
         *   @ NOT IMPLEMENTED YET!!!!!
         *   @ by archslaveCW
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Display UI and wait for user interaction


            } else {
                // Request missing location permission.
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_LOCATION);
            }
        } else {
            // Location permission has been granted, continue as usual.

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // IF permission was allowed

            } else {
                // IF Permission was denied or request was cancelled
            }
        }
    }
}
