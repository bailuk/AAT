package ch.bailu.aat_lib.map.tile.source;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjTileCacheOnly;
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
    public String getID(Tile tile, AppContext context) {
        return AppDirectory.getTileFile(genRelativeFilePath(tile, original.getName()),  context.getTileCacheDirectory()).getPath();
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


