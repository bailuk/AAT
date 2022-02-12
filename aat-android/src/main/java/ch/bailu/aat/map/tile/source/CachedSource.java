package ch.bailu.aat.map.tile.source;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat.services.cache.ObjTileCached;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.map.tile.source.Source;
import ch.bailu.aat_lib.service.cache.Obj;

public class CachedSource extends Source {
    private final Source source;

    public CachedSource(Source s) {
        source = s;
    }

    public String getName() {
        return "Cached" + source.getName();
    }

    @Override
    public String getID(Tile aTile, AppContext context) {
        return "Cached" + source.getID(aTile, context);
    }

    @Override
    public int getMinimumZoomLevel() {
        return source.getMinimumZoomLevel();
    }

    @Override
    public int getMaximumZoomLevel() {
        return source.getMaximumZoomLevel();
    }

    @Override
    public int getAlpha() {
        return source.getAlpha();
    }


    @Override
    public Obj.Factory getFactory(Tile tile) {
        return new ObjTileCached.Factory(tile, source);
    }

    @Override
    public boolean isTransparent() {
        return source.isTransparent();
    }


//    public final static Source CACHED_ELEVATION_COLOR = new Source(Source.ELEVATION_COLOR);
    public final static CachedSource CACHED_ELEVATION_HILLSHADE = new CachedSource(ElevationSource.ELEVATION_HILLSHADE);
    //public final static Source CACHED_MAPSFORGE = new Source(Source.MAPSFORGE);
}

