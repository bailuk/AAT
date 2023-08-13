package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.attributes.AltitudeDelta
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class DescendDescription(storage: StorageInterface) : AltitudeDescription(storage) {
    override fun getLabel(): String {
        return Res.str().d_descend()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(info.getAttributes().getAsFloat(AltitudeDelta.INDEX_DESCEND))
    }
}
