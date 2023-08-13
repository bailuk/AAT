package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

open class AverageSpeedDescription(storage: StorageInterface) : SpeedDescription(storage) {
    override fun getLabel(): String {
        return Res.str().average()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(info.getSpeed())
    }
}
