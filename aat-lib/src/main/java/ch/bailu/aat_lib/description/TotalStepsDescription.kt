package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.attributes.StepCounterAttributes
import ch.bailu.aat_lib.resources.Res

class TotalStepsDescription : ContentDescription() {
    private var value = VALUE_DISABLED
    private val unit: String = Res.str().sensor_step_total_unit()
    private val label: String = Res.str().sensor_step_total()

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
        val attr = info.getAttributes()
        value = if (attr.hasKey(StepCounterAttributes.KEY_INDEX_STEPS_TOTAL)) {
            info.getAttributes()[StepCounterAttributes.KEY_INDEX_STEPS_TOTAL]
        } else {
            VALUE_DISABLED
        }
    }
}
