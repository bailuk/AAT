package ch.bailu.aat_gtk.app

import ch.bailu.aat_lib.map.AppDensity

object GtkAppDensity : AppDensity() {
    override fun toPixelScaled_f(diPixel: Float): Float {
        return 0.7f * diPixel
    }
}