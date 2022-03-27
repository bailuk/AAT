package ch.bailu.aat_gtk.view.map

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.util.Point as UtilPoint
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.LatLong
import org.mapsforge.core.model.Point
import org.mapsforge.map.layer.Layer




class GtkLayerWrapper(
    private val mcontext: MapContext,
    private val layer: MapLayerInterface) : Layer() {

    override fun draw(
        boundingBox: BoundingBox,
        zoomLevel: Byte,
        canvas: Canvas,
        topLeftPoint: Point
    ) {
        layer.drawInside(mcontext)
    }

    override fun onTap(tapLatLong: LatLong, layerXY: Point?, tapXY: Point): Boolean {
        return layer.onTap(UtilPoint(tapXY.x, tapXY.y))
    }
}
