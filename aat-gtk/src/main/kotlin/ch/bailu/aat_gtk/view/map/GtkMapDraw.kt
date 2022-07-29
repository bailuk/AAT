package ch.bailu.aat_gtk.view.map

import ch.bailu.aat_gtk.app.GtkAppDensity
import ch.bailu.aat_lib.map.*
import ch.bailu.aat_lib.util.Point
import ch.bailu.aat_lib.util.Rect
import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.graphics.Paint
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory

class GtkMapDraw(private val nodeBitmap: NodeBitmap): MapDraw {
    private val SPACE = 5

    private var canvas: Canvas? = null

    private val res = GtkAppDensity
    val textPaint   = MapPaint.createStatusTextPaint(res)
    private val gridPaint   = MapPaint.createGridPaint(res)
    val legendPaint = MapPaint.createLegendTextPaint(res)

    private val textHeight   = textPaint.getTextHeight("X") + 5
    private val point_radius = res.toPixel_i(MapDraw.POINT_RADIUS.toFloat())

    private var left = 0
    private var top = 0
    private var bottom = 0
    private var right = 0


    fun init(c: Canvas, metric: MapMetrics) {
        canvas = c
        init(metric)
    }

    private fun init(metric: MapMetrics) {
        left = metric.left
        top = metric.top
        bottom = metric.bottom
        right = metric.right
    }

    override fun getGridPaint(): Paint {
        return gridPaint
    }

    override fun getNodeBitmap(): Bitmap? {
        return nodeBitmap.tileBitmap.tileBitmap
    }

    override fun grid(center: Point, space: Int) {
        var x = center.x
        while (x < right) {
            vLine(x)
            x += space
        }

        x = center.x - space
        while (x > left) {
            vLine(x)
            x -= space
        }

        var y = center.y
        while (y < bottom) {
            hLine(y)
            y += space
        }

        y = center.y - space
        while (y > top) {
            hLine(y)
            y -= space
        }
    }


    override fun vLine(x: Int) {
        canvas?.drawLine(x, top, x, bottom, gridPaint)
    }

    override fun hLine(y: Int) {
        canvas?.drawLine(left, y, right, y, gridPaint)
    }


    override fun point(pixel: Point) {
        circle(pixel, point_radius, gridPaint)
    }

    override fun textTop(text: String?, line: Int) {
        canvas?.drawText(text, left + SPACE, top + SPACE + textHeight * line, textPaint)
    }

    override fun textBottom(s: String?, line: Int) {
        canvas?.drawText(s, left + SPACE, bottom - SPACE - textHeight * (line + 1), textPaint)
    }


    override fun circle(pixel: Point, radius: Int, paint: Paint?) {
        canvas?.drawCircle(pixel.x, pixel.y, radius, paint)
    }

    override fun rect(rect: Rect, paint: Paint?) {
        canvas?.drawLine(rect.left, rect.top, rect.left, rect.bottom, paint)
        canvas?.drawLine(rect.left, rect.bottom, rect.right, rect.bottom, paint)
        canvas?.drawLine(rect.right, rect.bottom, rect.right, rect.top, paint)
        canvas?.drawLine(rect.right, rect.top, rect.left, rect.top, paint)
    }


    override fun bitmap(b: Bitmap, pixel: Point) {
        canvas?.drawBitmap(b, centerX(b,pixel), centerY(b,pixel))
    }

    override fun bitmap(b: Bitmap, pixel: Point, color: Int) {
        canvas?.drawBitmap(b, centerX(b,pixel), centerY(b,pixel))
    }

    private fun centerX(b: Bitmap, p: Point): Int {
        return center(b.width, p.x)
    }

    private fun centerY(b: Bitmap, p: Point): Int {
        return center(b.height, p.y)
    }

    private fun center(size: Int, pos: Int): Int {
        return pos - size / 2
    }

    override fun edge(nodes: TwoNodes, paint: Paint) {
        canvas?.drawLine(
            nodes.nodeA.pixel.x,
            nodes.nodeA.pixel.y,
            nodes.nodeB.pixel.x,
            nodes.nodeB.pixel.y,
            paint
        )
    }

    override fun label(text: String, pixel: Point, background: Paint, frame: Paint) {
        canvas?.drawText(text, pixel.x, pixel.y, legendPaint)
    }

    override fun createPaint(): Paint {
        return GtkGraphicFactory.INSTANCE.createPaint()
    }
}
