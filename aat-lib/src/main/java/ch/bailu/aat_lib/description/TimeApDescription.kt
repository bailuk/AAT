package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.resources.Res

class TimeApDescription : TimeDescription() {
    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val autoPause = info.attributes.getAsLong(AutoPause.INDEX_AUTO_PAUSE_TIME)
        setCache(info.timeDelta - autoPause)
    }

    override fun getLabel(): String {
        return Res.str().time_ap()
    }
}
