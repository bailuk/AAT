package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.util.Point

interface GraphCanvas {
    fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int)
    fun drawLine(pa: Point, pb: Point, color: Int)
    fun drawText(text: String, x: Int, y: Int)
    fun drawBitmap(pa: Point, color: Int)
    fun getTextSize(): Int
}
