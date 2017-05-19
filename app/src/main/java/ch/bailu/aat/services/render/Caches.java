package ch.bailu.aat.services.render;

import java.util.HashMap;

import ch.bailu.aat.services.cache.MapsForgeTileObject;
import ch.bailu.aat.util.ui.AppLog;

public class Caches {
    private HashMap<String, Cache> caches = new HashMap<>(5);


    public void lockToCache(MapsForgeTileObject o) {
        get(o.getThemeID()).lockToCache(o);
    }


    public void freeFromCache(MapsForgeTileObject o) {
        Cache cache = caches.get(o.getThemeID());

        if (cache == null) {
            AppLog.d(this, "FIXME: missing cache");
            return;
        }


        cache.freeFromCache(o);

        if (cache.isEmpty()) {
            //caches.remove(o.getThemeID());
            AppLog.d(this, o.getThemeID() + " is empty");
        }
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
