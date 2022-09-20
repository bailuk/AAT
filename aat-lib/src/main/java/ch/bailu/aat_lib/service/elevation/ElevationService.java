package ch.bailu.aat_lib.service.elevation;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.coordinates.Dem3Coordinates;
import ch.bailu.aat_lib.service.VirtualService;
import ch.bailu.aat_lib.service.elevation.loader.Dem3Loader;
import ch.bailu.aat_lib.service.elevation.loader.Dem3Tiles;
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdater;
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdaterClient;

public final class ElevationService extends VirtualService implements ElevationProvider, ElevetionServiceInterface {

    private final ElevationUpdater updater;
    private final Dem3Loader loader;

    public ElevationService(AppContext appContext) {
        Dem3Tiles tiles = new Dem3Tiles();
        loader = new Dem3Loader(appContext, appContext.createTimer(), tiles);
        updater = new ElevationUpdater(appContext, loader, tiles);
    }

    @Override
    public void requestElevationUpdates(ElevationUpdaterClient e, Dem3Coordinates[] c) {
        updater.requestElevationUpdates(e, c);
    }

    public void requestElevationUpdates() {
        updater.requestElevationUpdates();
    }


    public void cancelElevationUpdates(ElevationUpdaterClient e) {
        updater.cancelElevationUpdates(e);
    }

    @Override
    public short getElevation(int laE6, int loE6) {
        return loader.getElevation(laE6, loE6);
    }

    public void close() {
        updater.close();
        loader.close();
    }
}
