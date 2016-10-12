package ch.bailu.aat.views.map;

import android.graphics.drawable.Drawable;
import android.os.Handler;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.ITileSource;

public class NullTileProvider extends AbsTileProvider {


    @Override
    public Drawable getMapTile(MapTile pTile) {
        return null;
    }

    @Override
    public void detach() {}

    @Override
    public int getMinimumZoomLevel() {
        return MINIMUM_ZOOMLEVEL;
    }

    @Override
    public int getMaximumZoomLevel() {
        return MAXIMUM_ZOOMLEVEL;
    }

    @Override
    public void setTileRequestCompleteHandler(Handler handler) {
    }

    @Override
    public void ensureCapacity(int numNeeded) {
    }

    @Override
    public void reDownloadTiles() {
    }


}
