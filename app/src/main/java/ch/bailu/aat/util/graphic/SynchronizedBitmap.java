package ch.bailu.aat.util.graphic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.TileBitmap;

import ch.bailu.aat.services.cache.ObjectHandle;

public class SynchronizedBitmap implements BitmapContainer {

    private AppBitmap bitmap = null;
    private Drawable drawable = null;

    private long size = ObjectHandle.MIN_SIZE;


    public synchronized AppBitmap get() {
        return bitmap;
    }


    @Override
    public Bitmap getAndroidBitmap() {
        if (bitmap != null) return bitmap.getAndroidBitmap();
        return null;
    }

    @Override
    public org.mapsforge.core.graphics.Bitmap getBitmap() {
        if (bitmap != null)
            return bitmap.getBitmap();
        return null;
    }

    @Override
    public TileBitmap getTileBitmap() {
        if (bitmap != null) return bitmap.getTileBitmap();
        return null;
    }

    public synchronized Drawable getDrawable(Resources r) {

        if (drawable == null && bitmap != null) {
            drawable = bitmap.getDrawable(r);
        }
        return drawable;
    }



    @Override
    public Canvas getAndroidCanvas() {
        if (bitmap != null) return bitmap.getAndroidCanvas();
        return null;
    }


    public boolean load(String file, AppBitmap def) {
        Boolean r = load(file);
        if (r == false) set(def);
        return r;
    }

    public boolean load(String file) {
        Bitmap b = BitmapFactory.decodeFile(file);
        if (b != null) {
            set(b);
        }

        return b != null;
    }

    public synchronized void set(Bitmap b) {
        set(new AppBitmap(b));
    }


    public synchronized long getSize() {
        return size;
    }


    @Override
    public void free() {
        if (bitmap != null) {
            bitmap.free();
        }

        bitmap = null;
        drawable = null;
        size = ObjectHandle.MIN_SIZE;
    }


    public synchronized void set(AppBitmap b) {


        if (b != null) {
            free();
            bitmap = b;
            drawable = null;
            size=b.getSize();
        }
    }


    public static Bitmap createBitmap(int w, int h) {
        final Bitmap r = Bitmap.createBitmap(
                w,
                h,
                Bitmap.Config.ARGB_8888);
        r.eraseColor(Color.WHITE);


        return r;
    }
}
