package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.resources.Res

open class PauseDescription : TimeDescription() {
    override fun getLabel(): String {
        return Res.str().pause()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(info.getPause())
    }
}
