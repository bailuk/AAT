package ch.bailu.aat_gtk.util

import ch.bailu.aat_lib.util.Timer
import ch.bailu.gtk.glib.Glib

class GtkTimer : Timer {
    private var run: Runnable? = null

    private val timeout = Glib.OnSourceFunc { _, _ ->
        val r = run
        if (r is Runnable) {
            r.run()
        }
        false
    }

    override fun kick(run: Runnable, interval: Long) {
        this.run = run
        Glib.timeoutAdd(interval.toInt(), timeout, null)
    }

    override fun cancel() {
        run = null
    }
}
