package ch.bailu.aat.util.ui

import android.content.Context
import android.util.DisplayMetrics
import ch.bailu.aat_lib.map.AppDensity

open class AndroidAppDensity : AppDensity {
    private val density: Float
    private val scaledDensity: Float


    constructor(context: Context) : this(context.resources.displayMetrics)
    constructor(metrics: DisplayMetrics) : this(metrics.density, metrics.scaledDensity)
    constructor(d: Float = 1f, sd: Float = 1f) {
        density = d
        scaledDensity = sd
    }

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
