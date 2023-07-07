package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class AveragePaceDescriptionAP(s: StorageInterface) : AveragePaceDescription(s) {
    override fun getLabel(): String {
        return Res.str().pace_ap()
    }

    override fun getLabelShort(): String {
        return super.getLabel()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val apTime = info.attributes.getAsLong(AutoPause.INDEX_AUTO_PAUSE_TIME)
        val apDistance = info.attributes.getAsFloat(AutoPause.INDEX_AUTO_PAUSE_DISTANCE)
        val distance = info.distance - apDistance
        val sTime = (info.timeDelta - apTime) / 1000
        val fTime = sTime.toFloat()
        if (distance > 0f) setCache(fTime / distance) else setCache(0f)
    }
}
