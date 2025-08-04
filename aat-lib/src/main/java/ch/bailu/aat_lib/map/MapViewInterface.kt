package ch.bailu.aat_lib.map

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.model.MapViewPosition

interface MapViewInterface {
    fun frameBounding(boundingBox: BoundingBoxE6)
    fun zoomOut()
    fun zoomIn()
    fun requestRedraw()
    fun add(layer: MapLayerInterface)
    fun getMContext(): MapContext
    fun setZoomLevel(z: Byte)
    fun setCenter(gpsLocation: LatLong)
    fun reDownloadTiles()
    fun getMapViewPosition(): MapViewPosition
}
