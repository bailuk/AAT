package ch.bailu.aat_lib.description

abstract class DoubleDescription : ContentDescription() {
    protected var cache = 0.0
        private set

    protected fun setCache(f: Double): Boolean {
        val r: Boolean
        if (cache == f) {
            r = false
        } else {
            r = true
            cache = f
        }
        return r
    }
}
