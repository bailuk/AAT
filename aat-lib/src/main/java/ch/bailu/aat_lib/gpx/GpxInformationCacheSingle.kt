package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface

class GpxInformationCacheSingle {
    var info: GpxInformation = GpxInformation.NULL
        private set

    var infoID = InfoID.UNSPECIFIED
        private set

    fun set(infoID: Int, info: GpxInformation) {
        this.info = info
        this.infoID = infoID
    }

    fun reset() {
        set(InfoID.UNSPECIFIED, GpxInformation.NULL)
    }

    fun letUpdate(obj: OnContentUpdatedInterface) {
        obj.onContentUpdated(infoID, info)
    }
}
