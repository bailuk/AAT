package ch.bailu.aat.map

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.AppDensity
import ch.bailu.aat_lib.map.MapDraw
import ch.bailu.aat_lib.map.MapMetrics
import ch.bailu.aat_lib.map.MapPaint
import ch.bailu.aat_lib.map.NodeBitmap
import ch.bailu.aat_lib.map.TwoNodes
import ch.bailu.aat_lib.util.Point
import ch.bailu.aat_lib.util.Rect
import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.graphics.Paint
import org.mapsforge.map.android.graphics.AndroidGraphicFactory

class AndroidDraw(res: AppDensity, appContext: AppContext?) : MapDraw {
    var canvas: Canvas? = null
        private set

    private val textPaint: Paint
    private val gridPaint: Paint
    private val legendPaint: Paint
    private val bitmapCanvas = BitmapDraw()
    private val textHeight: Int
    private var left = 0
    private var top = 0
    private var bottom = 0
    private var right = 0
    private val pointRadius: Int
    private val nodePainter: NodeBitmap

    init {
        legendPaint = setFakeBoldText(MapPaint.createLegendTextPaint(res))
        gridPaint = MapPaint.createGridPaint(res)
        textPaint = setFakeBoldText(MapPaint.createStatusTextPaint(res))
        textHeight = textPaint.getTextHeight("X") + 5
        nodePainter = NodeBitmap.get(res, appContext)
        pointRadius = res.toPixelInt(MapDraw.POINT_RADIUS.toFloat())
    }

    private fun setFakeBoldText(p: Paint): Paint {
        To.androidPaint(p).isFakeBoldText = true
        return p
    }

    private fun init(metric: MapMetrics) {
        left = metric.left
        top = metric.top
        bottom = metric.bottom
        right = metric.right
    }

    fun init(c: Canvas, metric: MapMetrics) {
        canvas = c
        init(metric)
    }

    fun init(c: android.graphics.Canvas, metric: MapMetrics) {
        init(AndroidGraphicFactory.createGraphicContext(c), metric)
    }

    override fun getGridPaint(): Paint {
        return gridPaint
    }

    override fun getNodeBitmap(): Bitmap {
        return nodePainter.tileBitmap.bitmap
    }

    override fun grid(center: Point, space: Int) {
        run {
            var x = center.x
            while (x < right) {
                vLine(x)
                x += space
            }
        }
        var x = center.x - space
        while (x > left) {
            vLine(x)
            x -= space
        }
        run {
            var y = center.y
            while (y < bottom) {
                hLine(y)
                y += space
            }
        }
        var y = center.y - space
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
        circle(pixel, pointRadius, gridPaint)
    }

    override fun textTop(text: String, line: Int) {
        canvas?.drawText(text, left + SPACE, top + SPACE + textHeight * line, textPaint)
    }

    override fun textBottom(s: String, line: Int) {
        canvas?.drawText(s, left + SPACE, bottom - SPACE - textHeight * (line + 1), textPaint)
    }

    override fun circle(pixel: Point, radius: Int, paint: Paint) {
        canvas?.drawCircle(pixel.x, pixel.y, radius, paint)
    }

    override fun rect(rect: Rect, paint: Paint) {
        canvas?.drawLine(rect.left, rect.top, rect.left, rect.bottom, paint)
        canvas?.drawLine(rect.left, rect.bottom, rect.right, rect.bottom, paint)
        canvas?.drawLine(rect.right, rect.bottom, rect.right, rect.top, paint)
        canvas?.drawLine(rect.right, rect.top, rect.left, rect.top, paint)
    }

    override fun bitmap(b: Bitmap, p: Point, c: Int) {
        canvas?.apply {
            bitmapCanvas.draw(To.androidCanvas(this), To.androidBitmap(b), p, c)
        }

    }

    override fun bitmap(b: Bitmap, p: Point) {
        canvas?.apply {
            bitmapCanvas.draw(To.androidCanvas(this), To.androidBitmap(b), p)
        }

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
        drawBackground(text, pixel, background)
        drawBackground(text, pixel, frame)
        canvas?.drawText(text, pixel.x, pixel.y, legendPaint)
    }

    private fun drawBackground(text: String, pixel: Point, paint: Paint) {
        val lp = To.androidPaint(legendPaint)
        val legendMetrics = lp.fontMetrics

        val canvas = canvas

        if (canvas is Canvas) {
            To.androidCanvas(canvas).drawRect(
                pixel.x.toFloat(),
                pixel.y.toFloat() + legendMetrics.top - MapDraw.MARGIN,
                pixel.x.toFloat() + lp.measureText(text) + MapDraw.MARGIN * 2,
                pixel.y.toFloat() + legendMetrics.bottom + MapDraw.MARGIN,
                To.androidPaint(paint)
            )
        }
    }

    override fun createPaint(): Paint {
        return AndroidGraphicFactory.INSTANCE.createPaint()
    }

    companion object {
        private const val SPACE = 5


    }
}
