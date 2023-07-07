package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

open class AveragePaceDescription(s: StorageInterface) : PaceDescription(s) {
    override fun getLabel(): String {
        return Res.str().pace()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(speedToPace(info.getSpeed()))
    }
}
