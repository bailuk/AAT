package ch.bailu.aat.preferences.map;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.map.tile.source.MapsForgeSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.preferences.SolidBoolean;

public abstract class SolidEnableTileCache extends SolidBoolean {

    public SolidEnableTileCache(Context c, String key) {
        super(c,
                SolidEnableTileCache.class.getSimpleName() + key);
    }



    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_enable_cache);
    }


    public static class Hillshade extends SolidEnableTileCache {

        public Hillshade(Context c) {
            super(c,Source.ELEVATION_HILLSHADE.getName());
        }
    }

    public static class MapsForge extends SolidEnableTileCache {
        public MapsForge(Context c) {
            super(c, MapsForgeSource.NAME);
        }
    }
}
