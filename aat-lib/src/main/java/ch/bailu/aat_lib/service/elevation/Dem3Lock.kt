package ch.bailu.aat_lib.service.elevation

import ch.bailu.aat_lib.logger.AppLog.w

class Dem3Lock {
    private var locks: Int = 0

    fun lock() {
        locks++
    }

    fun free() {
        locks--
    }

    val isLocked: Boolean
        get() {
            if (locks < 0) {
                w(this, "Negative lock!!: $locks")
            }
            return locks > 0
        }
}
