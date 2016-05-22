package ch.bailu.aat.services.dem;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.MultiServiceLink.ServiceContext;

public interface ElevationUpdaterClient  {
    public SrtmCoordinates[] getSrtmTileCoordinates();
    
    public void updateFromSrtmTile(ServiceContext cs, Dem3Tile tile);
    
    public boolean isUpdating();

  
    
}
