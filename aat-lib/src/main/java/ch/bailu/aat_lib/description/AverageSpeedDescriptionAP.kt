package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class AverageSpeedDescriptionAP(storage: StorageInterface) : AverageSpeedDescription(storage) {
    override fun getLabel(): String {
        return Res.str().average_ap()
    }

    override fun getLabelShort(): String {
        return super.getLabel()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val apTime = info.getAttributes().getAsLong(AutoPause.INDEX_AUTO_PAUSE_TIME)
        val apDistance = info.getAttributes().getAsLong(AutoPause.INDEX_AUTO_PAUSE_DISTANCE)
        val distance = info.getDistance() - apDistance
        val sTime = (info.getTimeDelta() - apTime) / 1000
        val fTime = sTime.toFloat()
        if (fTime > 0f) setCache(distance / fTime) else setCache(0f)
    }
}
