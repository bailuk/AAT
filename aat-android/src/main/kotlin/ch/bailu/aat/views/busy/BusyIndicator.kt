package ch.bailu.aat.views.busy

import android.content.Context
import android.widget.ProgressBar

class BusyIndicator(context: Context) : ProgressBar(context) {
    init {
        isIndeterminate = true
        stopWaiting()
    }

    fun startWaiting() {
        visibility = VISIBLE
    }

    fun stopWaiting() {
        visibility = INVISIBLE
    }
}
