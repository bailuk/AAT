package ch.bailu.aat_lib.service.render;

import java.util.HashMap;

import ch.bailu.aat_lib.service.cache.ObjTileMapsForge;

public final class Caches {
    private final HashMap<String, Cache> caches = new HashMap<>(5);

    public void lockToRenderer(ObjTileMapsForge o) {
        get(o.getThemeID()).lockToRenderer(o);
    }
    public void freeFromRenderer(ObjTileMapsForge o) {
        get(o.getThemeID()).freeFromRenderer(o);
    }

    public Cache get(String themeID) {
        Cache cache = caches.get(themeID);

        if (cache == null) {
            cache = new Cache();
            caches.put(themeID, cache);
        }
        return cache;
    }
}
