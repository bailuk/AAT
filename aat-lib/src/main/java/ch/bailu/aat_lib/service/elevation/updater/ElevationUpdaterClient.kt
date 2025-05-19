package ch.bailu.aat_lib.service.elevation.updater;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile;

public interface ElevationUpdaterClient {
    void updateFromSrtmTile(AppContext appContext, Dem3Tile tile);
}

