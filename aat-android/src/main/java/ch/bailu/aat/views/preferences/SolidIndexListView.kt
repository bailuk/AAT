package ch.bailu.aat.views.preferences

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.preferences.dialog.SolidIndexListDialog
import ch.bailu.aat_lib.preferences.SolidIndexList

class SolidIndexListView(context: Context, private val solid: SolidIndexList, theme: UiTheme) :
    AbsSolidView(context, solid, theme) {
    override fun onRequestNewValue() {
        if (solid.length() < 3) {
            solid.cycle()
        } else {
            SolidIndexListDialog(context, solid)
        }
    }
}
