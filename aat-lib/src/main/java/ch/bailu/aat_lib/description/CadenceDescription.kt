package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.attributes.CadenceSpeedAttributes
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.sensor.SensorState


class CadenceDescription : ContentDescription() {
    private val labelDefault: String = Res.str().sensor_cadence()
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

    override fun onContentUpdated(iid: Int,  info: GpxInformation) {
        val haveSensor = SensorState.isConnected(InfoID.CADENCE_SENSOR)
        if (iid == InfoID.CADENCE_SENSOR && haveSensor) {
            val hasContact = info.getAttributes().getAsBoolean(CadenceSpeedAttributes.KEY_INDEX_CONTACT)
            label = if (hasContact) {
                labelDefault
            } else {
                labelWait
            }
            value = info.getAttributes()[CadenceSpeedAttributes.KEY_INDEX_CRANK_RPM]
        } else {
            label = labelDefault
            value = VALUE_DISABLED
        }
    }

    companion object {
        const val UNIT = "rpm"
    }
}
