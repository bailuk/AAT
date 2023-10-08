package ch.bailu.aat_lib.preferences.map;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public abstract class SolidEnableTileCache extends SolidBoolean {

    public SolidEnableTileCache(StorageInterface storageInterface, String key) {
        super(storageInterface, key);
    }



    @Nonnull
    @Override
    public String getLabel() {
        return Res.str().p_enable_cache();
    }


    public static class Hillshade extends SolidEnableTileCache {

        public Hillshade(StorageInterface c) {
            super(c,"CacheHillshade");
        }
    }

    public static class MapsForge extends SolidEnableTileCache {
        public MapsForge(StorageInterface c) {
            super(c, "CacheMapsForge");
        }
    }
}
