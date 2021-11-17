package ch.bailu.aat.services.elevation.updater;

import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile;
import ch.bailu.aat_lib.service.ServicesInterface;

public interface ElevationUpdaterClient {
    void updateFromSrtmTile(ServicesInterface cs, Dem3Tile tile);
}

