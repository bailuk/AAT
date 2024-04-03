package ch.bailu.aat_lib.map.layer.gpx

class DistanceCounter(limit1: Float, limit2: Float) {
    val min: Float = Math.min(limit1, limit2)
    val max: Float = Math.max(limit1, limit2)
    private var count = 0f

    fun add(x: Float) {
        count += x
    }

    fun reset() {
        count = 0f
    }

    val isTooSmall: Boolean
        get() = count < min

    fun hasDistance(): Boolean {
        return count >= min
    }

    val isTooLarge: Boolean
        get() = count > max
}
