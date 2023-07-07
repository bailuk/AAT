package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.attributes.SensorStateAttributes
import ch.bailu.aat_lib.resources.Res

class SensorStateDescription : StateDescription() {
    private var unit = ""
    override fun getLabel(): String {
        return Res.str().sensors()
    }

    override fun getUnit(): String {
        return unit
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        super.onContentUpdated(iid, info)
        val attributes = info.attributes
        if (attributes != null) {
            unit = info.attributes[SensorStateAttributes.KEY_SENSOR_OVERVIEW]
        }
    }
}
