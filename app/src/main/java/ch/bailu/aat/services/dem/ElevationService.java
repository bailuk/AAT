package ch.bailu.aat.services.dem;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.services.dem.loader.Dem3Loader;
import ch.bailu.aat.services.dem.loader.Dem3Tiles;
import ch.bailu.aat.services.dem.tile.ElevationProvider;
import ch.bailu.aat.services.dem.updater.ElevationUpdater;

public class ElevationService extends VirtualService implements ElevationProvider {


    private final ElevationUpdater updater;
    private final Dem3Tiles tiles = new Dem3Tiles();
    private final Dem3Loader loader;

    public ElevationService(ServiceContext sc) {
        super(sc);

        loader = new Dem3Loader(sc, tiles);
        updater = new ElevationUpdater(sc, loader, tiles);
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


    @Override
    public void appendStatusText(StringBuilder builder) {
        // TODO Auto-generated method stub

    }


}
