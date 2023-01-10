package ch.bailu.aat_gtk.util

import ch.bailu.aat_lib.util.Timer
import ch.bailu.gtk.glib.Glib

class GtkTimer : Timer {
    private var state = TimerState()

    override fun kick(run: Runnable, interval: Long) {
        val state = TimerState()
        this.state.enabled = false
        this.state = state

        Glib.timeoutAdd(interval.toInt(), { self, _ ->
            if (state.enabled) {
                run.run()
            }
            self.unregister()
            false
        }, null)
    }

    override fun cancel() {
        this.state.enabled = false
    }
}

private class TimerState {
    var enabled = true
}
