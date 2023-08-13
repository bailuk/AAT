package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.StateID

class PredictiveTimeDescription : TimeDescription() {
    private var time: Long = 0
    private var timePaused: Long = 0

    override fun getUnit(): String {
        return if (time - super.cache > SHOW_LABEL_LIMIT_MS) {
            super.getValue()
        } else super.getUnit()
    }

    override fun getValue(): String {
        return format(time)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        super.onContentUpdated(iid, info)
        val endTime = info.getEndTime()
        time = info.getTimeDelta()
        if (info.state != StateID.ON) {
            timePaused = time
        } else if (timePaused != time && endTime > 0) {
            time += System.currentTimeMillis() - endTime
        }
    }

    companion object {
        private const val SHOW_LABEL_LIMIT_MS = 5000
    }
}
