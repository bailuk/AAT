package ch.bailu.aat_lib.map.tile;

import org.mapsforge.core.graphics.TileBitmap;

public interface MapTileInterface {
    boolean isLoaded();

    void set(TileBitmap tileBitmap);

    void free();

    TileBitmap getTileBitmap();

    long getSize();
}
