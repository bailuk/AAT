package ch.bailu.aat.map.tile.source;

import android.content.Context;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat.services.cache.CachedTileObject;
import ch.bailu.aat.services.cache.ObjectHandle;

public class CachedSource extends Source {
    private final Source source;

    public CachedSource(Source s) {
        source = s;
    }

    @Override
    public String getName() {
        return "Cached" + source.getName();
    }

    @Override
    public String getID(Tile aTile, Context context) {
        return genID(aTile, getName());
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
    public int getPaintFlags() {
        return source.getPaintFlags();
    }

    public Source getSource() {
        return source;
    }

    @Override
    public ObjectHandle.Factory getFactory(Tile tile) {
        return new CachedTileObject.Factory(tile, source);
    }

    @Override
    public boolean isTransparent() {
        return source.isTransparent();
    }


//    public final static CachedSource CACHED_ELEVATION_COLOR = new CachedSource(Source.ELEVATION_COLOR);
    public final static CachedSource CACHED_ELEVATION_HILLSHADE = new CachedSource(Source.ELEVATION_HILLSHADE);
    public final static CachedSource CACHED_MAPSFORGE = new CachedSource(Source.MAPSFORGE);
}

