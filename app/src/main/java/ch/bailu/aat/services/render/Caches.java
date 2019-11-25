package ch.bailu.aat.services.render;

import java.util.HashMap;

import ch.bailu.aat.services.cache.MapsForgeTileObject;

public final class Caches {
    private final HashMap<String, Cache> caches = new HashMap<>(5);


    public void lockToRenderer(MapsForgeTileObject o) {
        get(o.getThemeID()).lockToRenderer(o);
    }


    public void freeFromRenderer(MapsForgeTileObject o) {
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
