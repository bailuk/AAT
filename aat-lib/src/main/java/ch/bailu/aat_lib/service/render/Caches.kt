package ch.bailu.aat_lib.service.render

import ch.bailu.aat_lib.service.cache.ObjTileMapsForge

class Caches {
    private val caches = HashMap<String, Cache>(5)

    fun lockToRenderer(o: ObjTileMapsForge) {
        get(o.themeID).lockToRenderer(o)
    }

    fun freeFromRenderer(o: ObjTileMapsForge) {
        get(o.themeID).freeFromRenderer(o)
    }

    fun get(themeID: String): Cache {
        var cache = caches[themeID]

        if (cache == null) {
            cache = Cache()
            caches[themeID] = cache
        }
        return cache
    }
}
