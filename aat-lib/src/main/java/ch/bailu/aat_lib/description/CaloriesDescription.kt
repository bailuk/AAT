package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidWeight
import ch.bailu.aat_lib.preferences.presets.SolidMET
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.aat_lib.resources.Res
import javax.annotation.Nonnull

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

    override fun onContentUpdated(iid: Int, @Nonnull info: GpxInformation) {
        setCache(calculateCalories(info).toLong())
    }

    private fun calculateCalories(track: GpxInformation): Float {
        val preset = SolidPreset(storage).index
        val hours = track.getTimeDelta().toFloat() / (1000f * 60f * 60f)
        val met = SolidMET(storage, preset).metValue
        val weight = SolidWeight(storage).value.toFloat()
        return hours * met * weight
    }
}
