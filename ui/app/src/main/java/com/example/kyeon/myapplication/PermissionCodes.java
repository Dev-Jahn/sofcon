package com.example.kyeon.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionCodes {
    // Request code for ACCESS_FINE_LOCATION
    protected static final int REQUEST_CODE_FINE_LOCATION = 3125;
    // Request code for ACCESS_COARSE_LOCATION
    protected static final int REQUEST_CODE_COARSE_LOCATION = 4125;
    // Request code for PLACE_PICK
    protected static final int REQUEST_PLACE_PICKER = 5125;


    protected static boolean getPermission(Context context, Activity activity, String permissionName, int requestCode) {
        if(ContextCompat.checkSelfPermission(context, permissionName)
                == PackageManager.PERMISSION_GRANTED) {
            // it means permission is granted
            return true;
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{ permissionName }, requestCode);
            return false;
        }
    }
}
