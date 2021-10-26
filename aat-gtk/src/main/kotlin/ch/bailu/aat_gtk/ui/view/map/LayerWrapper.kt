package ch.bailu.aat_gtk.ui.view.map

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.Point
import org.mapsforge.map.layer.Layer


class LayerWrapper(
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
}
