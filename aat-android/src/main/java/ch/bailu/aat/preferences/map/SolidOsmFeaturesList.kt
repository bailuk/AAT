package ch.bailu.aat.preferences.map

import android.content.Context
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.services.cache.osm_features.ObjMapFeatures
import ch.bailu.aat_lib.lib.filter_list.KeyList
import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.cache.CacheServiceInterface

class SolidOsmFeaturesList(c: Context?) : SolidBoolean(
    Storage(
        c!!
    ), SolidOsmFeaturesList::class.java.simpleName
) {
    override fun getLabel(): String {
        return Res.str().all()
    }

    fun getList(cacheService: CacheServiceInterface): ObjMapFeatures {
        var id = ObjMapFeatures.ID_SMALL
        if (isEnabled) id = ObjMapFeatures.ID_FULL
        return cacheService.getObject(id, ObjMapFeatures.Factory(id)) as ObjMapFeatures
    }

    companion object {
        private const val SMALL_LIST_KEYS = "amenity emergency leisure shop sport tourism name"
        @JvmStatic
        fun getKeyList(id: String): KeyList {
            return if (ObjMapFeatures.ID_SMALL == id) {
                KeyList(SMALL_LIST_KEYS)
            } else {
                KeyList()
            }
        }
    }
}
