package ch.bailu.aat.util.graphic;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.TileBitmap;



public interface BitmapContainer {
    android.graphics.Bitmap getAndroidBitmap();

    Bitmap getBitmap();
    TileBitmap getTileBitmap();
    Drawable getDrawable(Resources r);
    android.graphics.Canvas getAndroidCanvas();

    long getSize();
    void free();
}
