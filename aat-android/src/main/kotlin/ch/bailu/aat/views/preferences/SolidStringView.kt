package ch.bailu.aat.views.preferences

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.preferences.dialog.SolidStringDialog
import ch.bailu.aat_lib.preferences.SolidString

class SolidStringView(context: Context,
                      private val solid: SolidString,
                      theme: UiTheme) :
    AbsSolidView(context, solid, theme) {
    override fun onRequestNewValue() {
        SolidStringDialog(context, solid)
    }
}
