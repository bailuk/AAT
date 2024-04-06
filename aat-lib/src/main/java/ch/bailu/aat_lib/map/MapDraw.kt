package ch.bailu.aat_lib.map

import ch.bailu.aat_lib.util.Point
import ch.bailu.aat_lib.util.Rect
import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.core.graphics.Paint

interface MapDraw {
    fun getGridPaint(): Paint
    fun getNodeBitmap(): Bitmap
    fun grid(center: Point, space: Int)
    fun vLine(x: Int)
    fun hLine(y: Int)
    fun point(pixel: Point)
    fun textTop(text: String, line: Int)
    fun textBottom(s: String, line: Int)
    fun circle(pixel: Point, radius: Int, paint: Paint)
    fun rect(rect: Rect, paint: Paint)
    fun bitmap(bitmap: Bitmap, pixel: Point)
    fun bitmap(bitmap: Bitmap, pixel: Point, color: Int)
    fun edge(nodes: TwoNodes, paint: Paint)
    fun label(text: String, pixel: Point, background: Paint, frame: Paint)
    fun createPaint(): Paint

    companion object {
        const val POINT_RADIUS = 3
        const val MARGIN = 5
    }
}
