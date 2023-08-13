package ch.bailu.aat_gtk.util

import ch.bailu.aat_lib.util.Timer
import ch.bailu.gtk.glib.Glib

class GtkTimer : Timer {
    private var currentTimer: Timer = TimerCall()

    override fun kick(interval: Long, run: Runnable) {
        val newTimer: Timer = TimerCall()
        setTimer(newTimer)
        newTimer.kick(interval, run)
    }

    private fun setTimer(timer: Timer) {
        currentTimer.cancel()
        currentTimer = timer
    }

    override fun cancel() {
        this.currentTimer.cancel()
    }
}

private class TimerCall : Timer {
    private var enabled = true

    override fun kick(interval: Long, run: Runnable) {
        Glib.timeoutAdd(interval.toInt(), { self, _ ->
            if (enabled) {
                run.run()
            }
            enabled = false
            self.unregister()
            false
        }, null)
    }

    override fun cancel() {
        enabled = false
    }
}
