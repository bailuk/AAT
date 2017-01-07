package ch.bailu.aat.util.graphic;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.android.graphics.AndroidBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

public class AppBitmap implements BitmapContainer {

    private final Bitmap bitmap;


    public AppBitmap(Bitmap b) {
        bitmap = b;
    }

    public AppBitmap(int size) {
        this(size, false);
    }


    public AppBitmap(int size, boolean t) {
        this(size, size, t);
    }

    public AppBitmap(int w, int h, boolean t) {
        bitmap = AndroidGraphicFactory.INSTANCE.createBitmap(w, h, t);
    }

    public AppBitmap(android.graphics.Bitmap b) {
        bitmap = new AndroidBitmap(b);
    }


    @Override
    public android.graphics.Bitmap getAndroidBitmap() {
        return AndroidGraphicFactory.getBitmap(bitmap);
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }


    @Override
    public TileBitmap getTileBitmap() {
        return null;
    }

    @Override
    public Drawable getDrawable(Resources r) {
        return new BitmapDrawable(r, getAndroidBitmap());
    }

    @Override
    public long getSize() {
        return bitmap.getHeight() * bitmap.getWidth() * 4;
    }

    @Override
    public Canvas getAndroidCanvas() {
        return new Canvas(getAndroidBitmap());
    }

    @Override
    public void free() {
        bitmap.decrementRefCount();
    }


    public void erase() {
        android.graphics.Bitmap bitmap = getAndroidBitmap();
        if (bitmap != null) {
            bitmap.eraseColor(Color.WHITE);
        }
    }
}
