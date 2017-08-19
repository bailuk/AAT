package ch.bailu.aat.map.tile.source;

import android.content.Context;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjectHandle;

public class DoubleSource extends Source {

    private final Source sourceA, sourceB;
    private final int minZoomA;

    private final ServiceContext scontext;

    public DoubleSource(ServiceContext sc, Source a, Source b, int zoom) {
        sourceA = a;
        sourceB = b;

        minZoomA = Math.max(zoom, a.getMinimumZoomLevel());

        scontext = sc;
    }


    @Override
    public String getName() {
        return sourceA.getName();
    }

    @Override
    public String getID(Tile aTile, Context context) {
        return decide(aTile).getID(aTile, context);
    }


    private Source decide(Tile t) {
        Source r = sourceB;

        if (isZoomLevelSupportedA(t)) {
            if (scontext.lock()) {
                if (scontext.getRenderService().supportsTile(t))
                    r = sourceA;
                scontext.free();
            }
        }

        return r;
    }


    public boolean isZoomLevelSupportedA(Tile t) {
        return (t.zoomLevel <= sourceA.getMaximumZoomLevel() && t.zoomLevel >= minZoomA);
    }


    @Override
    public int getMinimumZoomLevel() {
        return sourceB.getMinimumZoomLevel();
    }

    @Override
    public int getMaximumZoomLevel() {
        return sourceA.getMaximumZoomLevel();
    }

    @Override
    public boolean isTransparent() {
        return sourceA.isTransparent();
    }

    @Override
    public int getAlpha() {
        return sourceA.getAlpha();
    }

    @Override
    public int getPaintFlags() {
        return sourceA.getPaintFlags();
    }

    @Override
    public ObjectHandle.Factory getFactory(Tile tile) {
        return decide(tile).getFactory(tile);
    }
}
