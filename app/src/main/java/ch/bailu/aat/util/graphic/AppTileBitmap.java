package ch.bailu.aat.util.graphic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

public class AppTileBitmap {
    private final TileBitmap bitmap;

    public AppTileBitmap(int size) {
        this(size, false);
    }


    public AppTileBitmap(int size, boolean isTransparent) {
        bitmap = AndroidGraphicFactory.INSTANCE.createTileBitmap(size, isTransparent);
    }


    public Bitmap getBitmap() {
        return AndroidGraphicFactory.getBitmap(bitmap);
    }
    public Canvas getCanvas() {
        return new Canvas(getBitmap());
    }

    public TileBitmap getTileBitmap() {
        return bitmap;
    }

    public void free() {
        bitmap.decrementRefCount();
    }

    public void erase() {
        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            bitmap.eraseColor(Color.WHITE);
        }
    }
}
