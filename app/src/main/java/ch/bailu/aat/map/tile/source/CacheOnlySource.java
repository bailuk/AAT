package ch.bailu.aat.map.tile.source;

import android.content.Context;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat.services.cache.CacheOnlyTileObject;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.util.fs.AppDirectory;

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
        return AppDirectory.getTileFile(genRelativeFilePath(tile, original.getName()), context).getPath();
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
    public int getPaintFlags() {
        return original.getPaintFlags();
    }

    @Override
    public TileObject.Factory getFactory(Tile tile) {
        return new CacheOnlyTileObject.Factory(tile, original.isTransparent());
    }

    @Override
    public boolean isTransparent() {
        return original.isTransparent();
    }
}


