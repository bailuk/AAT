package ch.bailu.aat.services.srtm;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.background.BackgroundService;

public interface ElevationUpdaterClient  {
    public SrtmCoordinates[] getSrtmTileCoordinates();
    
    public void updateFromSrtmTile(BackgroundService bg, Dem3Tile tile);
    
    public boolean isUpdating();

  
    
}
