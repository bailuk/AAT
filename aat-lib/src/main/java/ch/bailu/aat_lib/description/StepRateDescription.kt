package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.attributes.StepCounterAttributes
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.sensor.SensorState

class StepRateDescription : ContentDescription() {
    private var value = VALUE_DISABLED
    private var unit = UNIT
    private val label: String = Res.str().sensor_step_counter()

    override fun getValue(): String {
        return value
    }

    override fun getLabel(): String {
        return label
    }

    override fun getUnit(): String {
        return unit
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val haveSensor = SensorState.isConnected(InfoID.STEP_COUNTER_SENSOR)
        if (iid == InfoID.STEP_COUNTER_SENSOR && haveSensor) {
            value = info.getAttributes()[StepCounterAttributes.KEY_INDEX_STEPS_RATE]
            unit = UNIT + " (" + info.getAttributes()[StepCounterAttributes.KEY_INDEX_STEPS_TOTAL] + ")"
        } else {
            value = VALUE_DISABLED
        }
    }

    companion object {
        const val UNIT = "spm"
    }
}
