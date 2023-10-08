package ch.bailu.aat_gtk.app

import ch.bailu.aat_lib.map.AppDensity

class GtkAppDensity(private val density: Float = 1.0f, private val scaledDensity: Float = 0.8f) : AppDensity() {
    override fun toPixelFloat(diPixel: Float): Float {
        return density * diPixel
    }

    override fun toPixelScaledFloat(diPixel: Float): Float {
        return scaledDensity * diPixel
    }

    override fun toPixelInt(diPixel: Float): Int {
        return (toPixelFloat(diPixel) + 0.5f).toInt()
    }

    override fun toPixelInt(diPixel: Float, min: Int): Int {
        return Math.max(min, toPixelInt(diPixel))
    }

    override fun toDensityIndependentPixel(pixel: Float): Int {
        return (pixel / density).toInt()
    }
}
