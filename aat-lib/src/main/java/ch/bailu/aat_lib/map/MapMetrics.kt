package ch.bailu.aat_lib.map

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.coordinates.LatLongInterface
import ch.bailu.aat_lib.util.Point
import ch.bailu.aat_lib.util.Rect
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.LatLong

interface MapMetrics {
    fun getDensity(): AppDensity
    fun getLeft(): Int
    fun getRight(): Int
    fun getTop(): Int
    fun getBottom(): Int
    fun getWidth(): Int
    fun getHeight(): Int
    fun pixelToDistance(pixel: Int): Float
    fun distanceToPixel(meter: Float): Int
    fun getShortDistance(): Int
    fun getCenterPixel(): Point
    fun isVisible(box: BoundingBoxE6): Boolean
    fun isVisible(point: LatLongInterface): Boolean
    fun toMapPixels(box: BoundingBoxE6): Rect
    fun toPixel(tp: LatLongInterface): Point
    fun toPixel(p: LatLong): Point
    fun fromPixel(x: Int, y: Int): LatLong
    fun getBoundingBox(): BoundingBox
    fun getZoomLevel(): Int
}
