package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class DistanceApDescription(storage: StorageInterface) : DistanceDescription(storage) {
    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val apDistance = info.getAttributes().getAsFloat(AutoPause.INDEX_AUTO_PAUSE_DISTANCE)
        setCache(info.getDistance() - apDistance)
    }

    override fun getLabel(): String {
        return Res.str().distance_ap()
    }
}
