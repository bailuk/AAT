package ch.bailu.aat.util.graphic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.RectF;

import com.caverock.androidsvg.SVG;

import org.mapsforge.core.graphics.CorruptedInputStreamException;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import java.io.IOException;
import java.io.InputStream;

import ch.bailu.aat_lib.map.tile.MapTileInterface;
import ch.bailu.aat_lib.preferences.map.SolidTileSize;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.util.Rect;
import ch.bailu.foc.Foc;

public class AndroidSyncTileBitmap implements MapTileInterface {
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

    @Override
    public synchronized org.mapsforge.core.graphics.Canvas getCanvas() {
        if (bitmap != null) {
            return AndroidGraphicFactory.createGraphicContext(getAndroidCanvas());
        }
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
        long result = Obj.MIN_SIZE;
        Bitmap bitmap = getAndroidBitmap();

        if (bitmap != null) {
            result = (long) bitmap.getRowBytes() * bitmap.getHeight();
        }
        return result;
    }


    @Override
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


    @Override
    public synchronized void setBuffer(int[] buffer, Rect r) {
        initBitmap();
        Bitmap b = getAndroidBitmap();

        if (b != null) {
            b.setPixels(buffer, 0, r.width(), r.left, r.top, r.width(), r.height());
        }
    }

    private void initBitmap() {
        Bitmap b = getAndroidBitmap();
        if (b == null) {
            set(SolidTileSize.DEFAULT_TILESIZE, true);
            //b = getAndroidBitmap();
            //if (b != null) b.eraseColor(android.graphics.Color.TRANSPARENT);
        }
    }

    public synchronized void free() {
        if (bitmap != null) {
            bitmap.decrementRefCount();
        }
        bitmap = null;
        size = Obj.MIN_SIZE;
    }

    public boolean isLoaded() {
        return bitmap != null;
    }
}
