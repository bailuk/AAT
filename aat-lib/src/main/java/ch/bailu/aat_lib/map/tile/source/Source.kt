package ch.bailu.aat_lib.map.tile.source;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.cache.Obj;

public abstract class Source {
    public final static String EXT = ".png";

    public final static int TRANSPARENT = 150;
    public final static int OPAQUE = 255;

    public abstract String getName();
    public abstract String getID(Tile aTile, AppContext context);

    public abstract int getMinimumZoomLevel();
    public abstract int getMaximumZoomLevel();

    public abstract boolean isTransparent();
    public abstract int getAlpha();
    public abstract Obj.Factory getFactory(Tile tile);


    public boolean filterBitmap() {return false;}

    public static String genRelativeFilePath(final Tile tile, String name) {
        return  genID(tile,name) +  EXT;

    }

    public static String genID(Tile t, String name) {
        return name + "/" + t.zoomLevel + "/" + t.tileX + "/" + t.tileY;

    }
}
