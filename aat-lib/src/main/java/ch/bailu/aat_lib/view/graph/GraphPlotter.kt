package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.util.Point
import kotlin.math.max
import kotlin.math.min

class GraphPlotter(
    private val canvas: GraphCanvas,
    private val width: Int,
    private val height: Int,
    xScale: Float
) {
    private val textSize: Int = canvas.getTextSize()

    private val xScaler: Scaler = Scaler(width.toFloat(), xScale)
    private val yScaler: InvertedOffsetScaler = InvertedOffsetScaler(height)

    private var pointA = Point(-5, -5)
    private var pointB = Point(-5, -5)

    fun roundYScale(roundTo: Int) {
        yScaler.round(roundTo)
    }

    fun includeInYScale(value: Float) {
        yScaler.addValue(value)
    }

    fun drawYScale(lines: Int, factor: Float, drawFirstValue: Boolean) {
        var lines = lines
        lines = min(height / (textSize * 2), lines)
        lines = max(1, lines)

        var space = yScaler.getRealDistance() / lines
        space = max(1f, space)

        var x = yScaler.realOffset
        while (x <= yScaler.realTop) {
            drawHorizontalLine(x, factor, drawFirstValue)
            x += space
        }
    }

    private fun drawHorizontalLine(value: Float, factor: Float, drawFirstValue: Boolean) {
        val pixel = yScaler.scale(value).toInt()

        canvas.drawLine(0, pixel, width, pixel)

        if (drawFirstValue || pixel < height - textSize) drawScaleText(
            0,
            pixel,
            (value * factor).toInt().toString()
        )
    }

    private fun drawScaleText(x: Int, y: Int, value: String) {
        var x = x
        var y = y
        val w = value.length * textSize
        val h = textSize

        if ((x + w) > width) x = width - w
        if ((y - h) < 0) y = h

        canvas.drawText(value, x, y)
    }

    fun drawXScale(lines: Int, factor: Float, drawText: Boolean) {
        var lines = lines
        lines = min(width / (textSize * 4), lines)
        lines = max(1, lines)

        var space = xScaler.real / lines
        space = max(1f, space)

        var x = 0f
        while (x <= xScaler.real) {
            drawVerticalLine(x, factor, drawText)
            x += space
        }
    }

    private fun drawVerticalLine(value: Float, factor: Float, drawText: Boolean) {
        val pixel = xScaler.scale(value).toInt()

        canvas.drawLine(pixel, 0, pixel, height)

        if (drawText && pixel > textSize) {
            val text = (value * factor).toInt().toString()
            drawScaleText(pixel - textSize, height, text)
        }
    }


    fun plotData(xValue: Float, yValue: Float, color: Int): Int {
        scaleToPixel(xValue, yValue)

        val delta: Int = max(pointA.x - pointB.x, 0)

        if (delta > 1) {
            if (pointB.x < 0) pointB.y = pointA.y
            canvas.drawLine(pointB, pointA, color)
            switchPoints()
        }
        return delta
    }

    private fun scaleToPixel(xvalue: Float, yvalue: Float) {
        pointA.x = xScaler.scale(xvalue).toInt()
        pointA.y = yScaler.scale(yvalue).toInt()
    }

    private fun switchPoints() {
        val t = pointB
        pointB = pointA
        pointA = t
    }

    fun plotPoint(xvalue: Float, yvalue: Float, color: Int) {
        scaleToPixel(xvalue, yvalue)
        canvas.drawBitmap(pointA, color)
    }
}
