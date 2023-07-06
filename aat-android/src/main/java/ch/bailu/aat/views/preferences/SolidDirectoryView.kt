package ch.bailu.aat.views.preferences

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.preferences.dialog.SolidStringDialog
import ch.bailu.aat_lib.preferences.SolidFile

open class SolidDirectoryView(
    private val context: Context,
    protected val solid: SolidFile,
    theme: UiTheme
) : AbsSolidView(
    context, solid, theme
) {
    override fun onRequestNewValue() {
        SolidStringDialog(context, solid)
    }
}
