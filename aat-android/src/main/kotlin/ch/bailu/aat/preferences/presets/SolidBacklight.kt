package ch.bailu.aat.preferences.presets

import android.content.Context
import ch.bailu.aat.R
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat_lib.preferences.SolidStaticIndexList
import ch.bailu.aat_lib.resources.Res

class SolidBacklight(c: Context, i: Int) : SolidStaticIndexList(
    Storage(c), KEY + i, arrayOf(
        c.getString(R.string.p_backlight_off),
        c.getString(R.string.p_backlight_on),
        c.getString(R.string.p_backlight_on_no_lock)
    )
) {
    override fun getLabel(): String {
        return Res.str().p_backlight_title()
    }

    fun keepScreenOn(): Boolean {
        return index != 0
    }

    fun dismissKeyGuard(): Boolean {
        return index == 2
    }

    companion object {
        private const val KEY = "backlight"
    }
}
