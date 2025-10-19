package ch.bailu.aat.views.preferences

import android.app.Activity
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.preferences.dialog.SolidDirectoryDialog
import ch.bailu.aat_lib.preferences.SolidFile

class SolidDirectoryViewSAF(private val acontext: Activity,
                            private val solidFile: SolidFile,
                            theme: UiTheme) :
    AbsSolidView(acontext, solidFile, theme) {
    override fun onRequestNewValue() {
        SolidDirectoryDialog(acontext, solidFile)
    }
}
