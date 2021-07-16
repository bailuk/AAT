package ch.bailu.aat.preferences.map;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidBoolean;

public abstract class SolidEnableTileCache extends SolidBoolean {

    public SolidEnableTileCache(Context c, String key) {
        super(c, key);
    }



    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_enable_cache);
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
