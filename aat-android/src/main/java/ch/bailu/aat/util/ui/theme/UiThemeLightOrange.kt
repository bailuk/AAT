package ch.bailu.aat.util.ui.theme

import android.view.View

class UiThemeLightOrange(private val hl_color: Int) : UiThemeLight() {
    override fun button(v: View) {
        v.setBackgroundDrawable(AppTheme.getButtonDrawable(BUTTON_LIGHT_GRAY, hl_color))
    }
}
