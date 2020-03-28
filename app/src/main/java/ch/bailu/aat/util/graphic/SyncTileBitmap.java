package ch.bailu.aat.util.graphic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.RectF;

import com.caverock.androidsvg.SVG;

import org.mapsforge.core.graphics.CorruptedInputStreamException;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.util_java.foc.Foc;

public class SyncTileBitmap implements Closeable {
    private TileBitmap bitmap = null;

    private long size = 0;


    public synchronized TileBitmap getTileBitmap() {
        return bitmap;
    }


    public synchronized Bitmap getAndroidBitmap() {
        if (bitmap != null) return AndroidGraphicFactory.getBitmap(bitmap);
        return null;
    }


    public synchronized Canvas getAndroidCanvas() {
        Bitmap bitmap = getAndroidBitmap();
        if (bitmap != null) return new Canvas(bitmap);
        return null;
    }



    private static TileBitmap load(Foc file, int size, boolean transparent) {
        TileBitmap bitmap;
        InputStream inputStream = null;
        try {
            inputStream = file.openR();
            bitmap = AndroidGraphicFactory.INSTANCE.createTileBitmap(inputStream, size, transparent);
            bitmap.setTimestamp(file.lastModified());

        } catch (CorruptedInputStreamException | OutOfMemoryError | IOException e) {
            bitmap = null;
        } finally {
            Foc.close(inputStream);
        }
        return bitmap;
    }

    public synchronized void set(Foc file, int size, boolean transparent) {
        set(load(file, size, transparent));
    }


    public synchronized void set(TileBitmap b) {
        if (bitmap == b) return;

        free();
        bitmap = b;
        size = getSizeOfBitmap();
    }


    private long getSizeOfBitmap() {
        long result = ObjectHandle.MIN_SIZE;
        Bitmap bitmap = getAndroidBitmap();

        if (bitmap != null) {
            result = bitmap.getRowBytes() * bitmap.getHeight();
        }
        return result;
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


    public synchronized long getSize() {
        return size;
    }


    public synchronized void free() {
        if (bitmap != null) {
            bitmap.decrementRefCount();
        }
        bitmap = null;
        size = ObjectHandle.MIN_SIZE;
    }


    @Override
    public void close()  {
        free();
    }
}
