package ch.bailu.aat.map.mapsforge

import ch.bailu.aat_lib.map.MapViewInterface
import org.mapsforge.map.model.MapViewPosition

/**
 * Link to map views together.
 * Passive map takes zoom and position from active map
 */
class MapViewLinker(active: MapViewInterface, passive: MapsForgeViewBase) {
    private val active: MapViewPosition = active.getMapViewPosition()
    private val passive: MapViewPosition = passive.getMapViewPosition()
    private val onChange: ()->Unit = {
        setCenter()
        setZoom()
    }

    init {
        passive.isClickable = false
        this.active.addObserver(onChange)
    }

    private fun setZoom() {
        var zoom = active.zoomLevel + 2
        if (zoom > passive.zoomLevelMax) {
            zoom = active.zoomLevel - 4
        }

        zoom = minOf(passive.zoomLevelMax.toInt(), zoom)
        zoom = maxOf(passive.zoomLevelMin.toInt(), zoom)
        if (passive.zoomLevel.toInt() != zoom) {
            passive.zoomLevel = zoom.toByte()
        }
    }

    private fun setCenter() {
        // need to check to avoid circular notifications
        if (active.center != passive.center) {
            passive.center = active.center
        }
    }
}
