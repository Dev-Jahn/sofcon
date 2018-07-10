package ssu.cse.navigationcw;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Request code for ACCESS_FINE_LOCATION
    private static final int REQUEST_CODE_LOCATION = 3125;
    // Request code for PLACE_PICK
    private static final int REQUEST_PLACE_PICKER = 4125;

    private Button buttonMap;
    private Button buttonPlace;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonMap = (Button)findViewById(R.id.buttonMap);
        buttonMap.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        buttonPlace = (Button)findViewById(R.id.buttonPlace);
        buttonPlace.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        resources = getResources();

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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // this code is replacement of hard coded string
                // if you are not familiar with resources, look at strings.xml in res/values
                String announce = resources.getString(R.string.permission_request_announce1);
                announce += "\n";
                announce += resources.getString(R.string.permission_request_announce2);
                builder.setMessage(announce);

                builder.setPositiveButton(R.string.allow_permission,
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.setNegativeButton(R.string.disallow_permission, new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setCancelable(false);
                builder.show();
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
