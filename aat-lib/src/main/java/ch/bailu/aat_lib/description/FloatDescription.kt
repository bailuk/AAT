package ch.bailu.aat_lib.description

abstract class FloatDescription : ContentDescription() {
    protected var cache = 0f
        private set

    protected fun setCache(f: Float): Boolean {
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
