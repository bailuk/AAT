package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import android.content.Context;
import android.graphics.Bitmap;

public abstract class TileObject extends ObjectHandle{
    public static final int TILE_SIZE=256;
    
    
    
    public TileObject(String id) {
        super(id);
    }

    
    public abstract Bitmap getBitmap();

    public static abstract class  Source {
        public abstract TileBitmapFilter getBitmapFilter();
        public abstract String getName();
        public abstract String getID(MapTile aTile, Context context);
        public abstract int getMinimumZoomLevel();
        public abstract int getMaximumZoomLevel();
        
    
        public abstract ObjectHandle.Factory getFactory(MapTile mt);
        
        
    }
    
}
