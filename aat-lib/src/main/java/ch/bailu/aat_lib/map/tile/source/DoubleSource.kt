package ch.bailu.aat_lib.map.tile.source;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.cache.Obj;

public class DoubleSource extends Source {

    private final Source sourceA, sourceB;
    private final int minZoomA;

    private final ServicesInterface scontext;

    public DoubleSource(ServicesInterface sc, Source a, Source b, int zoom) {
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
    public String getID(Tile aTile, AppContext context) {
        return decide(aTile).getID(aTile, context);
    }


    private Source decide(final Tile t) {
        final Source[] r = {sourceB};

        if (isZoomLevelSupportedA(t)) {
            scontext.insideContext(() -> {
                if (scontext.getRenderService().supportsTile(t))
                    r[0] = sourceA;
            });
        }

        return r[0];
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
    public Obj.Factory getFactory(Tile tile) {
        return decide(tile).getFactory(tile);
    }
}
