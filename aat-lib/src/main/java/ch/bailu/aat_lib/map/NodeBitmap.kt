package ch.bailu.aat_lib.map

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.app.AppGraphicFactory.instance
import ch.bailu.aat_lib.lib.color.ARGB
import ch.bailu.aat_lib.map.MapPaint.createEdgePaintLine
import ch.bailu.aat_lib.map.tile.MapTileInterface
import org.mapsforge.core.graphics.Style

class NodeBitmap private constructor(radius: Int, res: AppDensity, context: AppContext) {
    val tileBitmap: MapTileInterface

    init {
        tileBitmap = context.createMapTile()
        val strokeWidth = res.toPixelInt(STROKE_WIDTH.toFloat())
        val hSize = radius + strokeWidth
        val size = hSize * 2
        tileBitmap[size] = true
        val canvas = tileBitmap.canvas
        val stroke = createEdgePaintLine(res)
        val fill = instance().createPaint()
        fill.setStyle(Style.FILL)
        fill.color = ARGB(150, ARGB.WHITE).toInt()
        canvas.drawCircle(hSize, hSize, radius, fill)
        canvas.drawCircle(hSize, hSize, radius, stroke)
    }

    companion object {
        private const val STROKE_WIDTH = MapPaint.EDGE_WIDTH_LINE
        private const val RADIUS = 6
        private val nodes = HashMap<Int, NodeBitmap>(10)
        operator fun get(res: AppDensity, context: AppContext): NodeBitmap {
            val radius = res.toPixelInt(RADIUS.toFloat())
            var node = nodes[radius]
            if (node == null) {
                node = NodeBitmap(radius, res, context)
                nodes[radius] = node
            }
            return node
        }
    }
}
