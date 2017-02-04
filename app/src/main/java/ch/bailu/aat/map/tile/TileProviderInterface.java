package ch.bailu.aat.map.tile;

import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.model.common.Observer;

import ch.bailu.aat.map.Attachable;
import ch.bailu.aat.map.tile.source.Source;

public interface TileProviderInterface extends Attachable {

    TileBitmap get(Tile tile);
    boolean contains(Tile tile);

    void addObserver(Observer observer);
    void removeObserver(Observer observer);

    int getCapacity();

    void setCapacity(int size);

    Drawable getDrawable(Tile convert);

    int getMaximumZoomLevel();

    int getMinimumZoomLevel();

    void reDownloadTiles();

    Source getSource();
}
