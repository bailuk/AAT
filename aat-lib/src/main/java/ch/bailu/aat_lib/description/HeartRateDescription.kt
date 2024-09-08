package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.attributes.HeartRateAttributes
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.sensor.SensorState

class HeartRateDescription : ContentDescription() {
    private val labelDefault: String = Res.str().sensor_heart_rate()
    private var value = VALUE_DISABLED
    private var label: String

    init {
        label = labelDefault
    }

    override fun getValue(): String {
        return value
    }

    override fun getLabel(): String {
        return label
    }

    override fun getUnit(): String {
        return UNIT
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val haveSensor = SensorState.isConnected(InfoID.HEART_RATE_SENSOR)
        if (iid == InfoID.HEART_RATE_SENSOR && haveSensor) {
            val bpm = info.getAttributes()[HeartRateAttributes.KEY_INDEX_BPM]
            val contact = info.getAttributes()[HeartRateAttributes.KEY_INDEX_CONTACT]
            value = bpm
            label = "$labelDefault $contact"
        } else {
            value = VALUE_DISABLED
            label = labelDefault
        }
    }

    companion object {
        const val UNIT = "bpm"
    }
}
