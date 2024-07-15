package ch.bailu.aat.views.msg.permission

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.foc.Foc

class DataDirectoryPermissionInfoView(context: Context, private val solid: SolidFile, theme: UiTheme) :  PermissionInfoView(context, theme),
    OnPreferencesChanged {

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        solid.register(this)
        updateText()
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            updateText()
        }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        solid.unregister(this)
    }

    private fun updateText() {
        updateText(getPermissionText(solid.getValueAsFile()))
    }

    companion object {
        private fun getPermissionText(file: Foc): String {
            var result = ""
            if (!file.exists()) {
                if (file.hasParent()) {
                    result = getPermissionText(file.parent())
                } else {
                    result = missingPermissionText
                }
            } else if (!file.canWrite() || !file.canRead()) {
                result = missingPermissionText
            }
            return result
        }

        private val missingPermissionText = ToDo.translate(
            "Data directory is missing permissions.\nChoose another directory or acquire permission with `Pick (SAF)…`\nPreferences -> General/System -> Data directory"
        )
    }
}
