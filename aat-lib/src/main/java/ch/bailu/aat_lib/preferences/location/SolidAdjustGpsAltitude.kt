package ch.bailu.aat_lib.preferences.location

import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import javax.annotation.Nonnull

class SolidAdjustGpsAltitude(storage: StorageInterface) : SolidBoolean(storage, KEY) {
    @Nonnull
    override fun getLabel(): String {
        return Res.str().p_adjust_altitude()
    }

    companion object {
        private const val KEY = "UseGpsAltitudeCorrection"
    }
}
