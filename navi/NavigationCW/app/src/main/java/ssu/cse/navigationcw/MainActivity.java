package ssu.cse.navigationcw;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Request code for ACCESS_FINE_LOCATION
    private static final int REQUEST_CODE_LOCATION = 3125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Check Permissions
         * - ACCESS_FINE_LOCATION
         *
         * @ IF permission is not allowed,
         *   We should show some dialogs that why we needs this permissions
         *   @ NOT IMPLEMENTED YET!!!!!
         *   @ by archslaveCW
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request missing location permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION);
        } else {
            // Location permission has been granted, continue as usual.

        }
    }
}
