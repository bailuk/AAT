package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.attributes.AltitudeDelta
import ch.bailu.aat_lib.resources.Res

class SlopeDescription : ContentDescription() {
    private var slope = "0"
    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        slope = info.getAttributes()[AltitudeDelta.INDEX_SLOPE]
    }

    override fun getValue(): String {
        return slope
    }

    override fun getUnit(): String {
        return "%"
    }

    override fun getLabel(): String {
        return Res.str().d_slope()
    }
}
