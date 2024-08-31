package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
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
        unit = info.getAttributes()[SensorStateAttributes.KEY_SENSOR_OVERVIEW]
    }
}
