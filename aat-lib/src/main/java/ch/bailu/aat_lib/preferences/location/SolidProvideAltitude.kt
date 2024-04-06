package ch.bailu.aat_lib.preferences.location

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res


class SolidProvideAltitude(storage: StorageInterface, unit: Int) : SolidAltitude(storage, KEY, unit) {

    override fun setValue(value: Int) {
        getStorage().writeIntegerForce(getKey(), value)
    }

    
    override fun getLabel(): String {
        return addUnit(Res.str().p_set_altitude())
    }

    companion object {
        private const val KEY = "ProvideAltitude"
    }
}
