package ch.bailu.aat_lib.service.elevation;

import java.util.List;

import ch.bailu.aat_lib.coordinates.Dem3Coordinates;
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdaterClient;

public interface ElevationServiceInterface {
    short getElevation(int latitudeE6, int longitudeE6);

    void requestElevationUpdates(ElevationUpdaterClient owner, List<Dem3Coordinates> srtmTileCoordinates);
    void requestElevationUpdates();

    void cancelElevationUpdates(ElevationUpdaterClient objGpxStatic);
}
