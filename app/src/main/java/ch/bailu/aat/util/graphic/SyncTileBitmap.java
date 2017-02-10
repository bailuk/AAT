package ch.bailu.aat.util.graphic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.caverock.androidsvg.SVG;

import org.mapsforge.core.graphics.CorruptedInputStreamException;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.util.IOUtils;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import ch.bailu.aat.services.cache.ObjectHandle;

public class SyncTileBitmap implements Closeable {
    private TileBitmap bitmap = null;
    private Drawable drawable = null;

    private long size = ObjectHandle.MIN_SIZE;


    public synchronized TileBitmap getTileBitmap() {
        return bitmap;
    }


    public synchronized Bitmap getAndroidBitmap() {
        if (bitmap != null) return AndroidGraphicFactory.getBitmap(bitmap);
        return null;
    }


    public synchronized Drawable getDrawable(Resources r) {
        if (drawable == null) {
            Bitmap bitmap = getAndroidBitmap();
            if (bitmap != null) {
                drawable = new BitmapDrawable(r, bitmap);
            }
        }
        return drawable;
    }



    public synchronized Canvas getAndroidCanvas() {
        Bitmap bitmap = getAndroidBitmap();
        if (bitmap != null) return new Canvas(bitmap);
        return null;
    }



    public static TileBitmap load(File file, int size, boolean transparent) {
        TileBitmap bitmap;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            bitmap = AndroidGraphicFactory.INSTANCE.createTileBitmap(inputStream, size, transparent);
            bitmap.setTimestamp(file.lastModified());

        } catch (CorruptedInputStreamException | IOException e) {
            bitmap = null;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return bitmap;
    }

    public synchronized void set(File file, int size, boolean transparent) {
        set(load(file, size, transparent));
    }


    public synchronized void set(TileBitmap b) {
        if (bitmap == b) return;

        free();
        bitmap = b;

        Bitmap bitmap = getAndroidBitmap();

        if (bitmap != null) {
            size = bitmap.getRowBytes() * bitmap.getHeight();
        } else {
            size = ObjectHandle.MIN_SIZE;
        }

    }

    public synchronized void set(int size, boolean transparent) {
        set(AndroidGraphicFactory.INSTANCE.createTileBitmap(size, transparent));
    }

    public synchronized void set(SVG svg, int size) {
        set(size, true);

        Picture p = svg.renderToPicture();
        Canvas c = getAndroidCanvas();

        if (c != null) {
            c.drawPicture(p, new RectF(0, 0, size, size));
        }
    }


    public static Drawable toDrawable(SVG svg, int size, Resources r, int color) {
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Picture p = svg.renderToPicture();
        Canvas c = new Canvas(bitmap);

        c.drawColor(color);
        c.drawPicture(p, new RectF(0,0,size,size));

        Drawable d = new BitmapDrawable(r, bitmap);

        d.setBounds(0,0,size,size);

        return d;
    }


    public synchronized long getSize() {
        return size;
    }



    public synchronized void free() {
        if (bitmap != null) {
            bitmap.decrementRefCount();
        }
        bitmap = null;
        drawable = null;
        size = ObjectHandle.MIN_SIZE;
    }


    @Override
    public void close()  {
        free();
    }
}
