package ch.bailu.aat_lib.map.layer.grid

import ch.bailu.aat_lib.coordinates.MeterCoordinates
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.LatLong

abstract class MeterGridLayer(storage: StorageInterface) : MapLayerInterface {
    private val grid = GridMetricScaler()
    private val distanceDescription: DistanceDescription = DistanceDescription(storage)
    private var ttext = ""
    private var btext = ""

    override fun drawForeground(mcontext: MapContext) {
        mcontext.draw().textTop(ttext, 1)
        mcontext.draw().textBottom(btext, 0)
    }

    override fun drawInside(mcontext: MapContext) {
        if (mcontext.getMetrics().getZoomLevel() > MIN_ZOOM_LEVEL) {
            grid.findOptimalScale(mcontext.getMetrics().getShortDistance() / 2)
            if (grid.optimalScale > 0) {
                val coordinates = getRoundedCoordinates(mcontext.getMetrics().getBoundingBox())
                val centerPixel = getCenterPixel(mcontext, coordinates)
                mcontext.draw().grid(
                    centerPixel,
                    mcontext.getMetrics().distanceToPixel(grid.optimalScale.toFloat())
                )
                mcontext.draw().point(centerPixel)
                ttext = distanceDescription.getDistanceDescription(grid.optimalScale.toFloat())
                btext = coordinates.toString()
            }
        }
    }

    private fun getCenterPixel(mcontext: MapContext, c: MeterCoordinates): Point {
        return mcontext.getMetrics().toPixel(c.toLatLong())
    }

    private fun getRoundedCoordinates(box: BoundingBox): MeterCoordinates {
        val center = box.centerPoint
        val c = getCoordinates(center)
        c.round(grid.optimalScale)
        return c
    }

    abstract fun getCoordinates(point: LatLong): MeterCoordinates

    companion object {
        private const val MIN_ZOOM_LEVEL = 5
    }
}
