package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res


abstract class SolidEnableTileCache(storage: StorageInterface, key: String) :
    SolidBoolean(storage, key) {
    
    override fun getLabel(): String {
        return Res.str().p_enable_cache()
    }

    class HillShade(storage: StorageInterface) : SolidEnableTileCache(storage, "CacheHillshade")
    class MapsForge(storage: StorageInterface) : SolidEnableTileCache(storage, "CacheMapsForge")
}
