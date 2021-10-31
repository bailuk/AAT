package ch.bailu.aat.map;

import android.content.Context;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.ui.AndroidAppDensity;
import ch.bailu.aat_lib.preferences.map.SolidTileSize;


public final class MapDensity extends AndroidAppDensity {

    private final int tileSize;

    public MapDensity(Context context) {
        super(context);

        tileSize = new SolidTileSize(new Storage(context), new AndroidAppDensity(context)).getTileSize();
    }

    public MapDensity() {
        tileSize = SolidTileSize.DEFAULT_TILESIZE;
    }

    public int getTileSize() {
        return tileSize;
    }
}
