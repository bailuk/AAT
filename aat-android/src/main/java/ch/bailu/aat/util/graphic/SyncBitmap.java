package ch.bailu.aat.util.graphic;

import com.caverock.androidsvg.SVG;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.map.android.graphics.AndroidBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.foc.Foc;

public class SyncBitmap {
    private Bitmap bitmap = null;

    private int size = Obj.MIN_SIZE;


    public SyncBitmap() {

    }
    public Bitmap getBitmap() {
        return bitmap;
    }




    private static Bitmap load(Foc file) throws IOException {
        android.graphics.Bitmap bitmap;
        InputStream in = null;

        try {
            in = new BufferedInputStream(file.openR());
            bitmap = android.graphics.BitmapFactory.decodeStream(in);

        } finally {
            Foc.close(in);
        }

        if (bitmap == null) throw new IOException(in.toString());

        return new AndroidBitmap(bitmap);
    }

    public synchronized void set(Foc file) throws IOException {
        set(load(file));
    }


    public synchronized void set(Bitmap b) {
        if (bitmap == b) return;
        free();
        bitmap = b;


        if (bitmap != null) {
            size = bitmap.getWidth() * bitmap.getHeight() + 4;
        } else {
            size = Obj.MIN_SIZE;
        }
    }


    public synchronized void set(int size, boolean transparent) {
        set(AndroidGraphicFactory.INSTANCE.createTileBitmap(size, transparent));
    }

    public synchronized void set(SVG svg, int size) {
        AndroidSyncTileBitmap b = new AndroidSyncTileBitmap();
        b.set(svg, size);
        set(b.getTileBitmap());
    }


    public synchronized long getSize() {
        return size;
    }




    public synchronized void free() {
        if (bitmap != null) {
            bitmap.decrementRefCount();
        }
        bitmap = null;
        size = Obj.MIN_SIZE;
    }
}
