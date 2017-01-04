package ch.bailu.aat.map.osmdroid;

import android.content.Context;

import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.preferences.SolidTileSize;
import ch.bailu.aat.services.cache.TileObject;


public class MapDensity extends AppDensity {

    private final int tileSize;

    public MapDensity(Context context) {
        super(context);

        tileSize = new SolidTileSize(context).getTileSize();
    }

    public MapDensity() {
        tileSize = TileObject.TILE_SIZE;
    }

    public int getTileSize() {
        return tileSize;
    }
}
