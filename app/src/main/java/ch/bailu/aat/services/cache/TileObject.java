package ch.bailu.aat.services.cache;

import android.content.Context;
import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat.services.ServiceContext;

public abstract class TileObject extends ObjectHandle {
    public static final int TILE_SIZE=256;
    

    
    public TileObject(String id) {
        super(id);
    }

    public abstract Bitmap getBitmap();
    public abstract TileBitmap getTileBitmap();
    public abstract Tile getTile();
    public abstract void reDownload(ServiceContext sc);

    public abstract boolean isLoaded();

    public static abstract class  Source {
        public abstract TileBitmapFilter getBitmapFilter();
        public abstract String getName();
        public abstract String getID(Tile aTile, Context context);


        public abstract int getMinimumZoomLevel();
        public abstract int getMaximumZoomLevel();
        
    
        public abstract ObjectHandle.Factory getFactory(Tile tile);


        public static String genID(Tile t, String name) {
            return name + "/" + t.zoomLevel + "/" + t.tileX + "/" + t.tileY;

        }
    }

    public static long getBytesHack(int size) {
        return size * size * 4;
    }
}
