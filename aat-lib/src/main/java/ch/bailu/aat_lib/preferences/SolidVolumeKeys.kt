package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.resources.Res

class SolidVolumeKeys(storage: StorageInterface) : SolidBoolean(storage, KEY) {

    override fun getLabel(): String {
        return Res.str().p_use_volume_keys()
    }

    companion object {
        private const val KEY = "USE_VOLUME_KEYS"
    }
}
