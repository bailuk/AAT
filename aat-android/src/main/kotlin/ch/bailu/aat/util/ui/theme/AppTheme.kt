package ch.bailu.aat.util.ui.theme

import android.R
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import android.view.View
import ch.bailu.aat.util.ui.AndroidAppDensity
import ch.bailu.aat_lib.app.AppColor

object AppTheme {
    // light & dark

    private const val hl_color = AppColor.HL_ORANGE

    @JvmField
    val bar: UiTheme = UiThemeDark(hl_color)
    val cockpit: UiTheme = UiThemeDark(hl_color)
    val preferences: UiTheme = UiThemeLightOrange(hl_color)
    val intro: UiTheme = UiThemeLightOrange(hl_color)
    val doc: UiTheme = UiThemeLightOrange(hl_color)

    @JvmField
    val search: UiTheme = UiThemeLightOrange(hl_color)

    @JvmField
    val trackList: UiTheme = UiThemeLightOrange(hl_color)
    val trackContent: UiTheme = UiThemeLightOrange(hl_color)
    val filter: UiTheme = UiThemeLightHeader(UiThemeLight.HL_BLUE)

    @JvmStatic
    @JvmOverloads
    fun padding(view: View, p: Int = 15) {
        val padding = AndroidAppDensity(view.context).toPixelInt(p.toFloat())
        view.setPadding(padding, padding, padding, padding)
    }

    @JvmStatic
    fun getButtonDrawable(def: Int, pressed: Int): Drawable {
        val result = StateListDrawable()
        result.addState(intArrayOf(R.attr.state_pressed), ColorDrawable(pressed))
        result.addState(StateSet.WILD_CARD, ColorDrawable(def))
        return result
    }
}
