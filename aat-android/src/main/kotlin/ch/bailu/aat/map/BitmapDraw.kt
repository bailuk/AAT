package ch.bailu.aat.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LightingColorFilter
import android.graphics.Paint
import ch.bailu.aat_lib.util.Point

class BitmapDraw {
    private val paint = Paint()
    private val colorPaint = Paint()

    init {
        paint.isFilterBitmap = true
        paint.isAntiAlias = true
        paint.isDither = true
    }

    fun draw(c: Canvas, b: Bitmap, p: Point) {
        c.drawBitmap(b, centerX(b, p).toFloat(), centerY(b, p).toFloat(), paint)
    }

    fun draw(c: Canvas, b: Bitmap, p: Point, color: Int) {
        colorPaint.colorFilter = LightingColorFilter(color, 0)
        c.drawBitmap(b, centerX(b, p).toFloat(), centerY(b, p).toFloat(), colorPaint)
    }

    companion object {
        private fun centerX(b: Bitmap, p: Point): Int {
            return center(b.width, p.x)
        }

        private fun centerY(b: Bitmap, p: Point): Int {
            return center(b.height, p.y)
        }

        private fun center(size: Int, pos: Int): Int {
            return pos - size / 2
        }
    }
}
