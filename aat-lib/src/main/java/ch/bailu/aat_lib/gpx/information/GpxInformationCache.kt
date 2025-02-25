package ch.bailu.aat_lib.gpx.information

import ch.bailu.aat_lib.dispatcher.TargetInterface

class GpxInformationCache {
    var info = GpxInformation.NULL
    var infoID = InfoID.UNSPECIFIED

    init {
        reset()
    }

    fun set(iid: Int, info: GpxInformation) {
        this.info = info
        infoID = iid
    }

    fun reset() {
        set(InfoID.UNSPECIFIED, GpxInformation.NULL)
    }

    fun letUpdate(obj: TargetInterface) {
        obj.onContentUpdated(infoID, info)
    }
}
