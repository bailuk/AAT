package ch.bailu.aat.preferences.map;

import android.content.Context;

import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.resources.Res;

public abstract class SolidEnableTileCache extends SolidBoolean {

    public SolidEnableTileCache(Context c, String key) {
        super(new Storage(c), key);
    }



    @Override
    public String getLabel() {
        return Res.str().p_enable_cache();
    }


    public static class Hillshade extends SolidEnableTileCache {

        public Hillshade(Context c) {
            super(c,"CacheHillshade");
        }
    }

    public static class MapsForge extends SolidEnableTileCache {
        public MapsForge(Context c) {
            super(c, "CacheMapsForge");
        }
    }
}
