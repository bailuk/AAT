package ch.bailu.aat_lib.map

import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.Dimension
import org.mapsforge.core.model.LatLong
import org.mapsforge.core.util.LatLongUtils

class MapDistances {
    private var meterPerOnePixel = 0f
    private var pixelPerOneMeter = 0f
    var shortDistance = 0f
        private set

    fun init(box: BoundingBox, dim: Dimension) {
        if (dim.height < dim.width) {
            val a = LatLong(box.minLatitude, box.maxLongitude)
            val b = LatLong(box.maxLatitude, box.maxLongitude)
            set(a, b, dim.height.toFloat())
        } else {
            val a = LatLong(box.maxLatitude, box.minLongitude)
            val b = LatLong(box.maxLatitude, box.maxLongitude)
            set(a, b, dim.width.toFloat())
        }
    }

    private operator fun set(a: LatLong, b: LatLong, pixel: Float) {
        val meter = LatLongUtils.sphericalDistance(a, b).toFloat()
        meterPerOnePixel = meter / pixel
        pixelPerOneMeter = pixel / meter
        shortDistance = meter
    }

    fun toDistance(pixel: Float): Float {
        return pixel * meterPerOnePixel
    }

    fun toPixel(meter: Float): Float {
        return meter * pixelPerOneMeter
    }
}
