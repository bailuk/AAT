package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.attributes.SampleRate
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidWeight
import ch.bailu.aat_lib.preferences.presets.SolidMET
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.aat_lib.resources.Res


/** Estimates calories from power meter data if available, otherwise from MET value and body weight. */
class CaloriesDescription(private val storage: StorageInterface) : LongDescription() {
    override fun getLabel(): String {
        return Res.str().calories()
    }

    override fun getUnit(): String {
        return "kcal"
    }

    override fun getValue(): String {
        return cache.toString()
    }

    override fun onContentUpdated(iid: Int,  info: GpxInformation) {
        setCache(calculateCalories(info).toLong())
    }

    private fun calculateCalories(track: GpxInformation): Float {
        val avgPower = track.getAttributes().getAsInteger(SampleRate.Power.INDEX_AVERAGE_POWER)
        if (avgPower > 0) {
            /* We have a power meter - that's certainly better than
               estimating with a user-specified MET value */
            val seconds = track.getTimeDelta().toFloat() / 1000f
            val kJ = avgPower * seconds / 1000f
            return kJ // 1 kJ mechanical work ≈ 1 kcal metabolic energy
        }

        val preset = SolidPreset(storage).index
        val hours = track.getTimeDelta().toFloat() / (1000f * 60f * 60f)
        val met = SolidMET(storage, preset).metValue
        val weight = SolidWeight(storage).getValue().toFloat()
        return hours * met * weight
    }
}
