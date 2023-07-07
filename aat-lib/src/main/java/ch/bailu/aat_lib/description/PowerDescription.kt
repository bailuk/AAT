package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.gpx.attributes.PowerAttributes
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.sensor.SensorState

class PowerDescription : ContentDescription() {
    private val labelDefault: String = Res.str().sensor_power()
    private val labelWait: String = "$labelDefault…"

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
        val haveSensor = SensorState.isConnected(InfoID.POWER_SENSOR)
        if (iid == InfoID.POWER_SENSOR && haveSensor) {
            val hasContact = info.attributes.getAsBoolean(PowerAttributes.KEY_INDEX_CONTACT)
            label = if (hasContact) {
                labelDefault
            } else {
                labelWait
            }
            value = info.attributes[PowerAttributes.KEY_INDEX_POWER]
        } else {
            label = labelDefault
            value = VALUE_DISABLED
        }
    }

    companion object {
        const val UNIT = "W"
    }
}
