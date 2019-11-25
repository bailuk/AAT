package ch.bailu.aat.map;

import android.content.Context;

import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.preferences.map.SolidTileSize;


public final class MapDensity extends AppDensity {

    private final int tileSize;

    public MapDensity(Context context) {
        super(context);

        tileSize = new SolidTileSize(context).getTileSize();
    }

    public MapDensity() {
        tileSize = SolidTileSize.DEFAULT_TILESIZE;
    }

    public int getTileSize() {
        return tileSize;
    }
}
