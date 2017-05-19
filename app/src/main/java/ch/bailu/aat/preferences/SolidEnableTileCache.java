package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.map.tile.source.MapsForgeSource;
import ch.bailu.aat.map.tile.source.Source;

public abstract class SolidEnableTileCache extends SolidBoolean {

    public SolidEnableTileCache(Context c, String key) {
        super(Storage.global(c),
                SolidEnableTileCache.class.getSimpleName() + key);
    }



    @Override
    public String getLabel() {
        return "Enable cache*";
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
