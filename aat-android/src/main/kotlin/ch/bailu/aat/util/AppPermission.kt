package ch.bailu.aat.util

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectoryDefault

object AppPermission {
    fun requestFromUser(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            requestFromUserSdk23(activity)
        }
    }

    private const val APP_PERMISSION = 99
    @TargetApi(23)
    private fun requestFromUserSdk23(activity: Activity) {
        activity.requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BODY_SENSORS,
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            APP_PERMISSION
        )
    }

    fun onRequestPermissionsResult(c: Context?, requestCode: Int) {
        if (requestCode == APP_PERMISSION) {
            AndroidSolidDataDirectoryDefault(c!!).setDefaultValue()
        }
    }

    fun checkLocation(context: Context): Boolean {
        return Build.VERSION.SDK_INT < 23 || checkLocationSdk23(context)
    }

    fun checkBackgroundLocation(context: Context): Boolean {
        return Build.VERSION.SDK_INT < 29 || checkBackgroundLocationSdk29(context)
    }

    @TargetApi(29)
    private fun checkBackgroundLocationSdk29(context: Context): Boolean {
        return checkSdk23(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    @TargetApi(23)
    private fun checkLocationSdk23(context: Context): Boolean {
        return checkSdk23(context, Manifest.permission.ACCESS_FINE_LOCATION) &&
                checkSdk23(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    @TargetApi(23)
    private fun checkSdk23(context: Context, permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }
}
