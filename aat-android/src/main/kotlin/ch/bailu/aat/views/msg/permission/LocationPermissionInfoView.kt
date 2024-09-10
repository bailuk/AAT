package ch.bailu.aat.views.msg.permission

import android.content.Context
import ch.bailu.aat.util.AppPermission
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.resources.ToDo

class LocationPermissionInfoView(context: Context, theme: UiTheme)
    : PermissionInfoView(context, theme) {

    init {
        setOnClickListener {
            updateText(getPermissionText())
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateText(getPermissionText())
    }

    private fun getPermissionText(): String {
        var result = ""

        if (AppPermission.checkLocation(context)) {
            if (!AppPermission.checkBackgroundLocation(context)) {
                result = noBackgroundPermissionText
            }
        } else {
            result = noPermissionText
        }
        return result
    }

    companion object {
        private val noBackgroundPermissionText = ToDo.translate(
            "No permission to access location from background\nUse Android app-specific settings to enable this permission"
        )
        private val noPermissionText = ToDo.translate(
            "No permission to access location"
        )
    }
}
