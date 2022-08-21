package ch.bailu.aat.util.graphic;

import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.RectF;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.CorruptedInputStreamException;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.map.tile.MapTileInterface;
import ch.bailu.aat_lib.preferences.map.SolidTileSize;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.util.Rect;
import ch.bailu.foc.Foc;

public class AndroidSyncTileBitmap implements MapTileInterface {
    private Bitmap bitmap = null;

    private long size = 0;

    @Override
    public TileBitmap getTileBitmap() {
        var b = bitmap;
        if (b instanceof TileBitmap) {
            return (TileBitmap) b;
        }
        return null;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }


    public android.graphics.Bitmap getAndroidBitmap() {
        var b = bitmap;
        if (b != null) {
            return AndroidGraphicFactory.getBitmap(b);
        }
        return null;
    }


    public Canvas getAndroidCanvas() {
        var b = getAndroidBitmap();
        if (b != null) {
            return new Canvas(b);
        }
        return null;
    }

    @Override
    public org.mapsforge.core.graphics.Canvas getCanvas() {
        var c = getAndroidCanvas();
        if (c != null) {
            return AndroidGraphicFactory.createGraphicContext(c);
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


    public synchronized void set(Foc file, int defaultTileSize, boolean transparent) {
        set(load(file, defaultTileSize, transparent));
    }

    @Override
    public void setSVG(Foc file, int size, boolean transparent) throws IOException {
        InputStream input = null;
        try {
            input = file.openR();
            SVG svg = SVG.getFromInputStream(input);
            set(svg, size);
        } catch (SVGParseException e) {
            throw new IOException(e.getMessage());
        } finally {
            Foc.close(input);
        }
    }


    public synchronized void set(Bitmap b) {
        if (bitmap == b) return;

        free();
        bitmap = b;
        size = getSizeOfBitmap();
    }


    private long getSizeOfBitmap() {
        var result = Obj.MIN_SIZE;
        var b = getAndroidBitmap();

        if (b != null) {
            result = b.getRowBytes() * b.getHeight();
        }
        return result;
    }


    @Override
    public synchronized void set(int defaultTileSize, boolean transparent) {
        set(AndroidGraphicFactory.INSTANCE.createTileBitmap(defaultTileSize, transparent));
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
    public synchronized void setBuffer(@Nonnull int[] buffer, @Nonnull Rect r) {
        initBitmap();
        var b = getAndroidBitmap();

        if (b != null) {
            b.setPixels(buffer, 0, r.width(), r.left, r.top, r.width(), r.height());
        }
    }

    private void initBitmap() {
        if (bitmap == null) {
            set(SolidTileSize.DEFAULT_TILESIZE, true);
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
