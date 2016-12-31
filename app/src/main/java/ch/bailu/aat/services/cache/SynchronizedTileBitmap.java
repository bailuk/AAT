package ch.bailu.aat.services.cache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.TileBitmap;

import ch.bailu.aat.mapsforge.MapsForgeBitmap;

public class SynchronizedTileBitmap {
    private MapsForgeBitmap bitmap = null;
    private Drawable drawable = null;

    private long size=ObjectHandle.MIN_SIZE;

    public synchronized Bitmap getBitmap() {
        if (bitmap != null)
            return bitmap.getBitmap();
        return null;
    }

    public synchronized TileBitmap getTileBitmap() {
        if (bitmap != null) return bitmap.getTileBitmap();
        return null;
    }


    public synchronized Drawable getDrawable(Resources r) {

        if (drawable == null && bitmap != null) {
            drawable = new BitmapDrawable(r, bitmap.getBitmap());
        }
        return drawable;
    }


    public synchronized void setSize() {
        Bitmap b = getBitmap();

        if (b == null) {
            size = ObjectHandle.MIN_SIZE;
        } else {
            size = b.getHeight() * b.getRowBytes();
        }

    }

    public synchronized long getSize() {
        return size;
    }


    public synchronized void set(MapsForgeBitmap b) {
        free();
        bitmap = b;
        setSize();
    }


    public synchronized void free() {
        if (bitmap != null) {
            bitmap.free();
        }

        bitmap = null;
        drawable = null;
        setSize();
    }
}
