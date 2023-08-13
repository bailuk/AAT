package ch.bailu.aat.views.msg

import android.content.Context
import android.widget.TextView
import ch.bailu.aat.util.AndroidTimer
import ch.bailu.aat.util.ui.AppLayout.fadeIn
import ch.bailu.aat.util.ui.AppLayout.fadeOut
import ch.bailu.aat_lib.map.MapColor

abstract class AbsMsgView(context: Context, private val displayForMillis: Int = DISPLAY_FOR_MILLIS) :
    TextView(context) {
    private val fadeOutTimer = AndroidTimer()

    companion object {
        const val DISPLAY_FOR_MILLIS = 6000
    }

    init {
        setBackgroundColor(MapColor.DARK)
        visibility = GONE
    }

    abstract fun attach()
    abstract fun detach()
    fun set(msg: String) {
        fadeOutTimer.cancel()
        text = msg
        if (visibility == GONE) fadeIn(this)
        fadeOutTimer.kick(displayForMillis.toLong()) { this.set() }
    }

    fun set() {
        if (visibility == VISIBLE) {
            fadeOut(this)
        }
    }
}
