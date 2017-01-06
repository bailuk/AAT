package ch.bailu.aat.map.osmdroid;

import android.graphics.drawable.Drawable;
import android.os.Handler;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.model.common.Observer;
import org.osmdroid.tileprovider.MapTile;

import ch.bailu.aat.map.tile.TileProviderInterface;

public class OsmTileProvider extends OsmTileProviderAbstract implements Observer {

    private final TileProviderInterface tileProvider;

    private Handler handler = new Handler();

    private final int tileSize;


    public OsmTileProvider(TileProviderInterface p, int tSize) {
        tileProvider = p;
        tileSize = tSize;
    }


    @Override
    public void reDownloadTiles() {
        tileProvider.reDownloadTiles();
    }

    @Override
    public int getMinimumZoomLevel() {
        return tileProvider.getMinimumZoomLevel();
    }

    @Override
    public int getMaximumZoomLevel() {
        return tileProvider.getMaximumZoomLevel();
    }

    @Override
    public Drawable getMapTile(MapTile pTile) {
        return tileProvider.getDrawable(convert(pTile, tileSize));
    }

    @Override
    public void setTileRequestCompleteHandler(Handler h) {
        handler = h;
    }

    @Override
    public void ensureCapacity(int numNeeded) {
        tileProvider.setCapacity(numNeeded);
    }

    @Override
    public void onAttached() {
        tileProvider.onAttached();
        tileProvider.addObserver(this);
    }

    @Override
    public void onDetached() {
        tileProvider.onDetached();
        tileProvider.removeObserver(this);
    }



    @Override
    public void onChange() {
        handler.sendEmptyMessage(MapTile.MAPTILE_SUCCESS_ID);
    }


    public static Tile convert(MapTile t, int tileSize) {
        return new Tile(t.getX(), t.getY(), (byte)t.getZoomLevel(), tileSize);
    }

}
