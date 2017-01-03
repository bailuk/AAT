package ch.bailu.aat.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class AppPermission {
    public static void requestFromUser(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            requestFromUserSdk23(activity);
        }

    }

    @TargetApi(23)
    private static void requestFromUserSdk23(Activity activity) {
        activity.requestPermissions(
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                99);
    }


    public static boolean checkLocation(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        } else {
            return checkLocationSdk23(context);
        }
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
