package ch.bailu.aat.map.osmdroid;

import android.graphics.drawable.Drawable;
import android.os.Handler;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.ITileSource;

import ch.bailu.aat.map.Attachable;


public abstract class AbsTileProvider implements OpenStreetMapTileProviderConstants, Attachable {

    /** Osmdroid compability **/
    public ITileSource getTileSource() {
        return null;
    }

    /** Osmdroid compability **/
    public void setTileSource(ITileSource s) {}

    public void attach() {
        this.onAttached();
    }

    public void detach() {
        this.onDetached();
    }
    
    
    public abstract void reDownloadTiles();
    public abstract int  getMinimumZoomLevel();
    public abstract int getMaximumZoomLevel();
    public abstract Drawable getMapTile(MapTile pTile);
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
