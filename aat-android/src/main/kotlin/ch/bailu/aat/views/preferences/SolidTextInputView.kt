package ch.bailu.aat.views.preferences

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.preferences.dialog.SolidTextInputDialog
import ch.bailu.aat_lib.preferences.AbsSolidType

class SolidTextInputView(
    context: Context,
    private val solid: AbsSolidType,
    private val inputType: Int,
    theme: UiTheme
) : AbsSolidView(context, solid, theme) {
    override fun onRequestNewValue() {
        SolidTextInputDialog(context, solid, inputType)
    }
}
