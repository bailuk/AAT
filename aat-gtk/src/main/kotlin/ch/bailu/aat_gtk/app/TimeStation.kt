package ch.bailu.aat_gtk.app

import ch.bailu.aat_lib.logger.AppLog

object TimeStation {
    private var time = System.currentTimeMillis()

    fun reset() {
        time = System.currentTimeMillis()
    }

    fun log(text: String) {
        val now = System.currentTimeMillis()
        val delta = now - time

        AppLog.d(this, "$delta - $text")
        time = now
    }
}
