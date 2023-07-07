package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.attributes.SampleRate
import ch.bailu.aat_lib.resources.Res

open class IndexedAttributeDescription(
    private val label: String,
    private val unit: String,
    private val keyIndex: Int
) : ContentDescription() {

    private var value = ""

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
        value = info.attributes[keyIndex]
    }

    class Cadence : IndexedAttributeDescription(
        Res.str().sensor_cadence(), CadenceDescription.UNIT,
        SampleRate.Cadence.INDEX_CADENCE
    )

    class TotalCadence : IndexedAttributeDescription(
        Res.str().sensor_cadence_total(),
        Res.str().sensor_cadence_total_unit(),
        SampleRate.Cadence.INDEX_TOTAL_CADENCE
    )

    class HeartRate : IndexedAttributeDescription(
        Res.str().sensor_heart_rate(),
        HeartRateDescription.UNIT,
        SampleRate.HeartRate.INDEX_AVERAGE_HR
    )

    class HeartBeats : IndexedAttributeDescription(
        Res.str().sensor_heart_beats(),
        Res.str().sensor_cadence_total_unit(),
        SampleRate.HeartRate.INDEX_HEART_BEATS
    )
}
