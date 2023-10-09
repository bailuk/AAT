package ch.bailu.aat_lib.map.layer

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.service.ServicesInterface
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.LatLong
import org.mapsforge.core.model.Point
import org.mapsforge.map.layer.Layer

class LayerWrapper(
    private val services: ServicesInterface,
    private val mcontext: MapContext,
    private val layer: MapLayerInterface
) : Layer() {
    override fun draw(bounding: BoundingBox, zoom: Byte, canvas: Canvas, topLeftPoint: Point) {
        services.insideContext { layer.drawInside(mcontext) }
    }

    override fun onTap(tapLatLong: LatLong, layerXY: Point, tapXY: Point): Boolean {
        return layer.onTap(ch.bailu.aat_lib.util.Point(tapXY.x, tapXY.y))
    }
}
