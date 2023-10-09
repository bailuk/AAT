package ch.bailu.aat.views.graph

import android.graphics.Canvas
import android.graphics.Paint
import ch.bailu.aat.map.BitmapDraw
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.AppDensity
import ch.bailu.aat_lib.map.NodeBitmap
import ch.bailu.aat_lib.util.Point
import ch.bailu.aat_lib.view.graph.GraphCanvas
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import kotlin.math.roundToInt

class AndroidCanvas(
    private val canvas: Canvas,
    context: AppContext,
    res: AppDensity,
    theme: UiTheme
) : GraphCanvas {
    private val paintFont: Paint = Paint()
    private val paintPlotLines: Paint
    private val paintLines: Paint
    private val bitmapCanvas = BitmapDraw()
    private val nodeBitmap: NodeBitmap

    init {
        paintFont.isAntiAlias = true
        paintFont.isDither = false
        paintFont.color = theme.getGraphTextColor()
        paintFont.textSize =
            res.toPixelScaledFloat(TEXT_SIZE.toFloat())
        paintPlotLines = Paint()
        paintPlotLines.isAntiAlias = true
        paintPlotLines.isDither = false
        paintPlotLines.strokeWidth = res.toPixelFloat(2f)
        paintLines = Paint()
        paintLines.isAntiAlias = true
        paintLines.isDither = false
        paintLines.strokeWidth = 0f
        paintLines.color = theme.getGraphLineColor()
        nodeBitmap = NodeBitmap.get(res, context)
    }

    override fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int) {
        canvas.drawLine(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), paintLines)
    }

    override fun drawLine(pA: Point, pB: Point, color: Int) {
        paintPlotLines.color = color
        canvas.drawLine(
            pA.x.toFloat(),
            pA.y.toFloat(),
            pB.x.toFloat(),
            pB.y.toFloat(),
            paintPlotLines
        )
    }

    override fun drawText(text: String, x: Int, y: Int) {
        canvas.drawText(text, x.toFloat(), y.toFloat(), paintFont)
    }

    override fun drawBitmap(point: Point, color: Int) {
        val mfBitmap = nodeBitmap.tileBitmap.getBitmap()
        if (mfBitmap != null) {
            val androidBitmap = AndroidGraphicFactory.getBitmap(mfBitmap)
            if (androidBitmap != null) {
                bitmapCanvas.draw(canvas, androidBitmap, point, color)
            }
        }
    }

    override fun getTextSize(): Int {
        return Math.max(paintFont.textSize.roundToInt(), 1)
    }

    companion object {
        private const val TEXT_SIZE = 15
    }
}
