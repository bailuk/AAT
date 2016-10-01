package ch.bailu.aat.services.dem;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.ServiceContext;

public interface ElevationUpdaterClient  {
    SrtmCoordinates[] getSrtmTileCoordinates();
    
    void updateFromSrtmTile(ServiceContext cs, Dem3Tile tile);
    
    //boolean isUpdating();
}
