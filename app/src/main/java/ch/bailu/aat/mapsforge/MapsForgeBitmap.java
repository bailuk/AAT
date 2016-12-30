package ch.bailu.aat.mapsforge;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

public class MapsForgeBitmap {

    private final TileBitmap bitmap;


    public MapsForgeBitmap(int size) {
        bitmap = AndroidGraphicFactory.INSTANCE.createTileBitmap(size, false);

        getBitmap().eraseColor(Color.WHITE);
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
}
