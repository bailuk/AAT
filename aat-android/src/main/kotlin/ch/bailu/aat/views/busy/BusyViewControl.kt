package ch.bailu.aat.views.busy

import android.view.View
import android.view.ViewGroup
import ch.bailu.aat_lib.util.IndexedMap

open class BusyViewControl(parent: ViewGroup) {
    val busy: BusyViewContainer
    private val busyMap = IndexedMap<Int, Boolean>()

    init {
        busy = BusyViewContainer(parent.context)
        parent.addView(
            busy,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        busy.visibility = View.GONE
    }

    @JvmOverloads
    fun startWaiting(id: Int = DEFAULT_ID) {
        changeWaiting(id, true)
    }

    @JvmOverloads
    fun stopWaiting(id: Int = DEFAULT_ID) {
        changeWaiting(id, false)
    }

    private fun changeWaiting(id: Int, w: Boolean) {
        busyMap.put(id, w)
        if (isWaiting()) {
            busy.visibility = View.VISIBLE
            busy.bringToFront()
        } else {
            busy.visibility = View.GONE
        }
    }

    fun isWaiting(): Boolean {
        for (i in 0 until busyMap.size()) {
            if (true == busyMap.getValueAt(i)) {
                return true
            }
        }
        return false
    }

    fun setText(text: CharSequence) {
        busy.setText(text)
    }

    companion object {
        private const val DEFAULT_ID = -1
    }
}
