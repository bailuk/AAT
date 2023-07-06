package ch.bailu.aat.util

import android.os.Handler
import android.os.Looper
import ch.bailu.aat_lib.util.Timer

@Suppress("DEPRECATION")
class AndroidTimer : Timer {
    private val handler: Handler
    private var run: Runnable? = null


    init {
        if (Looper.myLooper() == null) {
            Looper.prepare()
        }
        handler = Handler()
    }

    override fun kick(interval: Long, run: Runnable) {
        cancel()
        handler.postDelayed(run, interval)
        this.run = run
    }

    override fun cancel() {
        val run = this.run
        if (run != null) {
            handler.removeCallbacks(run)
            this.run = null
        }
    }
}
