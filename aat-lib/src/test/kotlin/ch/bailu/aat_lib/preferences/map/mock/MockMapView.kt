package ch.bailu.aat_lib.preferences.map.mock

import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.Dimension
import org.mapsforge.core.model.LatLong
import org.mapsforge.core.model.MapPosition
import org.mapsforge.core.model.Rotation
import org.mapsforge.map.layer.Layer
import org.mapsforge.map.layer.LayerManager
import org.mapsforge.map.model.Model
import org.mapsforge.map.scalebar.MapScaleBar
import org.mapsforge.map.util.MapPositionUtil
import org.mapsforge.map.util.MapViewProjection
import org.mapsforge.map.view.FpsCounter
import org.mapsforge.map.view.FrameBuffer
import org.mapsforge.map.view.MapView


class MockMapView : MapView {
    var mockDimension = Dimension(0,0)
    var mockModel = Model().apply {
        frameBufferModel.mapPosition = MapPosition(LatLong(0.0,0.0), 14)
    }


    override fun addLayer(layer: Layer?) {
        TODO("Not yet implemented")
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override fun destroyAll() {
        TODO("Not yet implemented")
    }

    override fun getBoundingBox(): BoundingBox? {
        return MapPositionUtil.getBoundingBox(
            this.model.mapViewPosition.mapPosition, Rotation.NULL_ROTATION, this.model.displayModel.tileSize,
            dimension, 0f, 0f
        )
    }

    override fun getDimension(): Dimension {
        return mockDimension
    }

    override fun getFpsCounter(): FpsCounter {
        TODO("Not yet implemented")
    }

    override fun getFrameBuffer(): FrameBuffer {
        TODO("Not yet implemented")
    }

    override fun getHeight(): Int {
        return mockDimension.height
    }

    override fun getLayerManager(): LayerManager {
        TODO("Not yet implemented")
    }

    override fun getMapRotation(): Rotation {
        return Rotation.NULL_ROTATION
    }

    override fun getMapScaleBar(): MapScaleBar {
        TODO("Not yet implemented")
    }

    override fun getMapViewCenterX(): Float {
        TODO("Not yet implemented")
    }

    override fun getMapViewCenterY(): Float {
        TODO("Not yet implemented")
    }

    override fun getMapViewProjection(): MapViewProjection {
        return MapViewProjection(this)
    }

    override fun getModel(): Model {
        return mockModel
    }

    override fun getOffsetX(): Float {
        TODO("Not yet implemented")
    }

    override fun getOffsetY(): Float {
        TODO("Not yet implemented")
    }

    override fun getWidth(): Int {
        return mockDimension.width
    }

    override fun repaint() {
        TODO("Not yet implemented")
    }

    override fun rotate(rotation: Rotation?) {
        TODO("Not yet implemented")
    }

    override fun setCenter(center: LatLong?) {
        TODO("Not yet implemented")
    }

    override fun setMapScaleBar(mapScaleBar: MapScaleBar?) {
        TODO("Not yet implemented")
    }

    override fun setMapViewCenterX(mapViewCenterY: Float) {
        TODO("Not yet implemented")
    }

    override fun setMapViewCenterY(mapViewCenterY: Float) {
        TODO("Not yet implemented")
    }

    override fun setZoomLevel(zoomLevel: Byte) {
        TODO("Not yet implemented")
    }

    override fun setZoomLevelMax(zoomLevelMax: Byte) {
        TODO("Not yet implemented")
    }

    override fun setZoomLevelMin(zoomLevelMin: Byte) {
        TODO("Not yet implemented")
    }

}
