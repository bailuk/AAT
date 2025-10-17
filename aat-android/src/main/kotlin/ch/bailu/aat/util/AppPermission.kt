package ch.bailu.aat.util

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import ch.bailu.aat.BuildConfig
import ch.bailu.aat.broadcaster.AndroidBroadcaster
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory
import ch.bailu.aat_lib.broadcaster.AppBroadcaster

object AppPermission {
    fun requestFromUser(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            requestFromUserSdk23(activity)
        }

        if (Build.VERSION.SDK_INT >= 30) {
            requestFromUserSdk30(activity)
        }
    }

    private const val APP_PERMISSION_23 = 923

    @TargetApi(23)
    private fun requestFromUserSdk23(activity: Activity) {
        activity.requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BODY_SENSORS,
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ),
            APP_PERMISSION_23
        )
    }

    private const val APP_PERMISSION_30 = 930

    @TargetApi(30)
    private fun requestFromUserSdk30(activity: Activity) {
        val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
        val permission = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION

        activity.startActivityForResult(Intent(permission, uri), APP_PERMISSION_30)
    }

    fun onRequestPermissionsResult(context: Context?, requestCode: Int) {
        if (context is Context) {
            if (requestCode == APP_PERMISSION_23 || requestCode == APP_PERMISSION_30) {
                AndroidSolidDataDirectory(context).setDefaultValue()
            }
            AndroidBroadcaster(context).broadcast(AppBroadcaster.PERMISSION_UPDATED)
        }
    }

    fun checkLocation(context: Context): Boolean {
        return Build.VERSION.SDK_INT < 23 || checkLocationSdk23(context)
    }

    fun checkBackgroundLocation(context: Context): Boolean {
        return Build.VERSION.SDK_INT < 29 || checkBackgroundLocationSdk29(context)
    }

    fun checkNotification(context: Context): Boolean {
        return Build.VERSION.SDK_INT < 33 || checkNotificationSdk33(context)
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

    @TargetApi(33)
    fun checkNotificationSdk33(context: Context): Boolean {
        return checkSdk23(context, Manifest.permission.POST_NOTIFICATIONS)
    }
}
