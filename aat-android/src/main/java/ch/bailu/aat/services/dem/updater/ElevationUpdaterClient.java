package ch.bailu.aat.services.dem.updater;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.dem.tile.Dem3Tile;

public interface ElevationUpdaterClient {
    //SrtmCoordinates[] getSrtmTileCoordinates();

    void updateFromSrtmTile(ServiceContext cs, Dem3Tile tile);
}

