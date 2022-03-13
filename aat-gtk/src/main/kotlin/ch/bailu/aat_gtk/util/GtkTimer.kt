package ch.bailu.aat_gtk.util

import ch.bailu.aat_lib.util.Timer
import ch.bailu.gtk.GTK
import ch.bailu.gtk.glib.Glib

class GtkTimer : Timer {
    private var run: Runnable? = null

    private val timeout = Glib.OnSourceFunc {
        val r = run
        if (r is Runnable) {
            r.run()
        }
        GTK.FALSE
    }

    override fun kick(run: Runnable, interval: Long) {
        this.run = run
        Glib.timeoutAdd(interval.toInt(), timeout, null)
    }

    override fun cancel() {
        run = null
    }
}
