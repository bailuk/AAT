package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class MaximumSpeedDescription(storage: StorageInterface) : SpeedDescription(storage) {
    override fun getLabel(): String {
        return Res.str().maximum()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(info.getAttributes().getAsFloat(MaxSpeed.INDEX_MAX_SPEED))
    }
}
