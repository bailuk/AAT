package ch.bailu.aat.views.preferences

import android.app.Activity
import android.view.View
import android.view.View.OnClickListener
import ch.bailu.aat.util.AppPermission
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.layout.LabelTextView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.util.ui.ToolTipProvider

class CheckPermissionsView(private val appContext: AppContext,
                           private val activityContext: Activity,
                           theme: UiTheme) :
    LabelTextView(activityContext, ToDo.translate("Check permissions"), theme),
    OnClickListener, ToolTipProvider
{
    init {
        setOnClickListener(this)
        theme.button(this)
        setToolTip(this)
    }

    private val onPermissionUpdated: BroadcastReceiver = BroadcastReceiver { setToolTip(this) }
    override fun onAttachedToWindow() {
        appContext.broadcaster.register(AppBroadcaster.PERMISSION_UPDATED, onPermissionUpdated)
        setToolTip(this)
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        appContext.broadcaster.unregister(onPermissionUpdated)
        super.onDetachedFromWindow()
    }
    override fun onClick(view: View) {
        setToolTip(this)
    }

    override fun getToolTip(): String? {
        var result = ""

        if (!AppPermission.checkNotification(activityContext)) {
            result = addPermissionText(result, noNotificationPermissionText)
        }

        if (!AppPermission.checkBluetoothPermission(activityContext)) {
            result = addPermissionText(result, noBluetoothPermission)
        }

        return addPermissionText(result, getPermissionText())
    }


    private fun addPermissionText(result: String, add: String): String {
        if (result.isNotEmpty() && add.isNotEmpty()) {
            return "$result\n$add"
        } else if (add.isNotEmpty()) {
            return add
        }
        return result
    }

    private fun getPermissionText(): String {
        if (!AppPermission.checkLocation(activityContext)) {
            return noPermissionText
        }
        if (!AppPermission.checkBackgroundLocation(activityContext)) {
            return noBackgroundPermissionText
        }
        return permissionOk
    }

    companion object {
        private val noNotificationPermissionText = ToDo.translate(
            "No permission to display notifications\nUse Android app-specific settings to enable this permission"
        )
        private val noBackgroundPermissionText = ToDo.translate(
            "No permission to access location from background\nUse Android app-specific settings to enable this permission"
        )
        private val noPermissionText = ToDo.translate(
            "No permission to access location"
        )
        private val permissionOk = ToDo.translate("Access location OK")

        private val noBluetoothPermission = ToDo.translate("Missing Bluetooth permission (Check 'Nearby Device' in Android app-specific settings")
    }
}
