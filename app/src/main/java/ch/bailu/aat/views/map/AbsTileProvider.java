package ch.bailu.aat.views.map;

import android.graphics.drawable.Drawable;
import android.os.Handler;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.ITileSource;

public abstract class AbsTileProvider implements OpenStreetMapTileProviderConstants  {


    public abstract void setTileSource(ITileSource s);


    
    
    public abstract void deleteVisibleTilesFromDisk();
    public abstract ITileSource getTileSource();
    public abstract int  getMinimumZoomLevel();
    public abstract int getMaximumZoomLevel();
    public abstract Drawable getMapTile(MapTile pTile);
    public abstract void detach();
    public abstract void  setTileRequestCompleteHandler(Handler handler);


    private long time;

    public void setStartTime() {
        time = System.currentTimeMillis();
    }

    public long getStartTime() {
        return time;
    }


    public abstract void ensureCapacity(int numNeeded);
}
