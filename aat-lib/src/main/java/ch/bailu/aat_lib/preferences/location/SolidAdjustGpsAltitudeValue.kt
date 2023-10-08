package ch.bailu.aat_lib.preferences.location

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.resources.Res
import javax.annotation.Nonnull

class SolidAdjustGpsAltitudeValue : SolidAltitude {
    constructor(storage: StorageInterface) : super(storage, KEY, SolidUnit.SI)
    constructor(storage: StorageInterface, unit: Int) : super(storage, KEY, unit)

    @Nonnull
    override fun getLabel(): String {
        return Res.str().p_adjust_altitude_by()
    }

    companion object {
        const val KEY = "AltitudeCorrection"
    }
}
