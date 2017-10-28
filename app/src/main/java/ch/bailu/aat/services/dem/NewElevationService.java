package ch.bailu.aat.services.dem;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.services.dem.loader.Dem3Loader;
import ch.bailu.aat.services.dem.loader.Dem3Tiles;
import ch.bailu.aat.services.dem.tile.ElevationProvider;
import ch.bailu.aat.services.dem.updater.ElevationUpdaterClient;
import ch.bailu.aat.services.dem.updater.NewElevationUpdater;

public class NewElevationService extends VirtualService implements ElevationProvider {
    private final NewElevationUpdater updater;
    private final Dem3Loader loader;


    public NewElevationService(ServiceContext sc) {
        super(sc);

        Dem3Tiles tiles = new Dem3Tiles();
        loader = new Dem3Loader(sc, tiles);
        updater = new NewElevationUpdater(sc, loader, tiles);

    }

    @Override
    public void appendStatusText(StringBuilder builder) {

    }



    public void requestElevationUpdates(ElevationUpdaterClient e) {
        updater.requestElevationUpdates(e);
    }


    public void cancelElevationUpdates(ElevationUpdaterClient e) {
        updater.cancelElevationUpdates(e);
    }


    
    @Override
    public short getElevation(int laE6, int loE6) {
        return loader.getElevation(laE6, loE6);
    }


    @Override
    public void close() {
        updater.close();
        loader.close();

    }

}
