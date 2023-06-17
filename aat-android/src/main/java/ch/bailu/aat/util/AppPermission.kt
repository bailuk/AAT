package ch.bailu.aat.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import ch.bailu.aat.preferences.system.AndroidSolidDataDirectoryDefault;

public class AppPermission {
    public static void requestFromUser(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            requestFromUserSdk23(activity);
        }

    }

    private final static int APP_PERMISSION=99;

    @TargetApi(23)
    private static void requestFromUserSdk23(Activity activity) {
        activity.requestPermissions(
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BODY_SENSORS,
                        Manifest.permission.ACTIVITY_RECOGNITION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                APP_PERMISSION);
    }


    public static void onRequestPermissionsResult (Context c, int requestCode) {
        if (requestCode == AppPermission.APP_PERMISSION) {
            new AndroidSolidDataDirectoryDefault(c).setDefaultValue();
        }
    }


    public static boolean checkLocation(Context context) {
        return Build.VERSION.SDK_INT < 23 || checkLocationSdk23(context);
    }



    @TargetApi(23)
    private static boolean checkLocationSdk23(Context context) {
        return (checkSdk23(context, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                checkSdk23(context, android.Manifest.permission.ACCESS_COARSE_LOCATION));
    }




    @TargetApi(23)
    private static boolean checkSdk23(Context context, String permission) {
        return (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }
}
