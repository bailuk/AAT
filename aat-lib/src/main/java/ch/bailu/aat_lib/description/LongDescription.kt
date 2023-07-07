package ch.bailu.aat_lib.description

abstract class LongDescription : ContentDescription() {
    protected var cache: Long = 0
        private set

    protected fun setCache(v: Long): Boolean {
        val r: Boolean
        if (cache == v) {
            r = false
        } else {
            r = true
            cache = v
        }
        return r
    }
}
