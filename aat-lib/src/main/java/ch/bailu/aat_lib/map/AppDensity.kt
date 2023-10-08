package ch.bailu.aat_lib.map

abstract class AppDensity {
    open fun toPixelFloat(diPixel: Float): Float {
        return diPixel
    }

    open fun toPixelScaledFloat(diPixel: Float): Float {
        return diPixel
    }

    open fun toPixelInt(diPixel: Float): Int {
        return (toPixelFloat(diPixel) + 0.5f).toInt()
    }

    open fun toPixelInt(diPixel: Float, min: Int): Int {
        return Math.max(min, toPixelInt(diPixel))
    }

    open fun toDensityIndependentPixel(pixel: Float): Int {
        return pixel.toInt()
    }
}
