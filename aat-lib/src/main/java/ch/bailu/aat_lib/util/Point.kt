package ch.bailu.aat_lib.util

import kotlin.math.roundToInt

/**
 * A point holding two integer coordinates.
 * To keep some code independent from UI libraries.
 */
class Point {
    @JvmField
    var x: Int
    @JvmField
    var y: Int

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    constructor() {
        y = 0
        x = y
    }

    constructor(x: Float, y: Float) {
        this.x = x.roundToInt()
        this.y = y.roundToInt()
    }

    constructor(x: Double, y: Double) {
        this.x = x.toFloat().roundToInt()
        this.y = y.toFloat().roundToInt()
    }

    fun set(p: Point) {
        x = p.x
        y = p.y
    }

    override fun toString(): String {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}'
    }
}
