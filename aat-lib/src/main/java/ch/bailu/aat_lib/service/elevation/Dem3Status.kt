package ch.bailu.aat_lib.service.elevation

class Dem3Status {
    var status: Int = EMPTY
        set(s) {
            field = s
            if (s == LOADING) stamp = System.currentTimeMillis()
        }
    var stamp: Long = System.currentTimeMillis()
        private set

    companion object {
        const val LOADING: Int = 1
        const val VALID: Int = 2
        const val EMPTY: Int = 3
    }
}
