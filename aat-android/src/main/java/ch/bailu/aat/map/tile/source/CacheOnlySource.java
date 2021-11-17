package ch.bailu.aat.map.tile.source;

import android.content.Context;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat.preferences.map.AndroidSolidTileCacheDirectory;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat.services.cache.ObjTileCacheOnly;
import ch.bailu.aat_lib.util.fs.AppDirectory;

public class CacheOnlySource extends Source {

    private final Source original;

    public CacheOnlySource(Source o) {
        original = o;
    }

    public String getName() {
        return original.getName();
    }

    @Override
    public String getID(Tile tile, Context context) {
        return AppDirectory.getTileFile(genRelativeFilePath(tile, original.getName()), new AndroidSolidTileCacheDirectory(context)).getPath();
    }


    @Override
    public int getMinimumZoomLevel() {
        return original.getMinimumZoomLevel();
    }

    @Override
    public int getMaximumZoomLevel() {
        return original.getMaximumZoomLevel();
    }

    @Override
    public int getAlpha() {
        return original.getAlpha();
    }


    @Override
    public Obj.Factory getFactory(Tile tile) {
        return new ObjTileCacheOnly.Factory(tile, original);
    }

    @Override
    public boolean isTransparent() {
        return original.isTransparent();
    }
}


