package ch.bailu.aat_lib.map.tile;

import org.mapsforge.core.graphics.TileBitmap;

import ch.bailu.foc.Foc;

public interface MapTileInterface {
    boolean isLoaded();

    void set(TileBitmap tileBitmap);
    void set(Foc file, int defaultTilesize, boolean transparent);

    void free();

    TileBitmap getTileBitmap();

    long getSize();

}
