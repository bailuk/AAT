package ch.bailu.aat_gtk.app

import ch.bailu.aat_lib.map.AppDensity

class GtkAppDensity(private val density: Float = 1.0f, private val scaledDensity: Float = 0.8f) : AppDensity() {
    override fun toPixel_f(diPixel: Float): Float {
        return density * diPixel
    }

    override fun toPixelScaled_f(diPixel: Float): Float {
        return scaledDensity * diPixel
    }

    override fun toPixel_i(diPixel: Float): Int {
        return (toPixel_f(diPixel) + 0.5f).toInt()
    }

    override fun toPixel_i(diPixel: Float, min: Int): Int {
        return Math.max(min, toPixel_i(diPixel))
    }

    override fun toDensityIndependentPixel(pixel: Float): Int {
        return (pixel / density).toInt()
    }
}
