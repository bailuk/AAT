package ch.bailu.aat_lib.view.graph

import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min


class InvertedOffsetScaler(pixel: Int) {
    private val scaler: Scaler = Scaler(pixel.toFloat())

    var realOffset: Float = Float.Companion.MAX_VALUE
        private set
    var realTop: Float = 0f
        private set

    fun addValue(v: Float) {
        this.realOffset = min(this.realOffset, v)
        this.realTop = max(this.realTop, v)
        scaler.init(this.realTop - this.realOffset)
    }

    fun scale(v: Float): Float {
        return scaler.scale - (scaler.scale(v - this.realOffset))
    }

    fun getRealDistance(): Float {
        return this.realTop - this.realOffset
    }

    fun round(roundTo: Int) {
        var d = (this.realTop / roundTo).toDouble()
        d = floor(d)
        this.realTop = (d * roundTo).toFloat() + roundTo

        d = (this.realOffset / roundTo).toDouble()
        d = floor(d)
        this.realOffset = d.toFloat() * roundTo

        addValue(this.realTop)
        addValue(this.realOffset)
    }
}
