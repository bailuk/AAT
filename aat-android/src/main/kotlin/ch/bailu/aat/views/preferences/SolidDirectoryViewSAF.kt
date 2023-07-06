package ch.bailu.aat.views.preferences

import android.app.Activity
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.preferences.dialog.SolidDirectoryDialog
import ch.bailu.aat_lib.preferences.SolidFile

class SolidDirectoryViewSAF(private val acontext: Activity, solidFile: SolidFile, theme: UiTheme) :
    SolidDirectoryView(acontext, solidFile, theme
    ) {
    override fun onRequestNewValue() {
        SolidDirectoryDialog(acontext, solid)
    }
}
