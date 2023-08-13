package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.resources.Res

class EndDateDescription : DateDescription() {
    override fun getLabel(): String {
        return Res.str().d_enddate()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(info.getEndTime())
    }
}
