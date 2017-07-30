package ch.bailu.aat.map.mapsforge;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.Dimension;

import java.util.logging.Logger;


public class FrameBufferBitmap {
    private static final Logger LOGGER = Logger.getLogger(FrameBufferBitmap.class.getName());

    private final Lock frameLock = new Lock();

    private Bitmap bitmap = null;

    private BitmapRequest bitmapRequest = null;
    private final Object bitmapRequestSync = new Object();



    public Bitmap lock() {
        synchronized (frameLock) {
            createBitmapIfRequested();

            if (bitmap != null) {
                frameLock.enable();
            }
            return bitmap;
        }
    }

    private void createBitmapIfRequested() {
        synchronized(bitmapRequestSync) {
            if (bitmapRequest != null) {

                destroyBitmap();
                bitmap = bitmapRequest.create();

                bitmapRequest = null;
            }
        }
    }


    public void release() {
        synchronized(frameLock) {
            frameLock.disable();
        }
    }


    public void create(GraphicFactory factory, Dimension dimension, int color, boolean isTransparent) {
        synchronized(bitmapRequestSync) {
            bitmapRequest = new BitmapRequest(factory, dimension, color, isTransparent);
        }
    }


    public void destroy()  {
        synchronized(frameLock) {
            if (bitmap != null) {
                frameLock.waitDisabled();
                destroyBitmap();
            }
        }

    }


    private void destroyBitmap() {
        if (bitmap != null) {
            bitmap.decrementRefCount();
            bitmap = null;
        }
    }


    public static void swap(FrameBufferBitmap a, FrameBufferBitmap b) {
        Bitmap t = a.bitmap;
        a.bitmap = b.bitmap;
        b.bitmap = t;

        BitmapRequest r = a.bitmapRequest;
        a.bitmapRequest = b.bitmapRequest;
        b.bitmapRequest = r;
    }


    public static class Lock {
        private boolean enabled = false;


        public synchronized void disable() {
            enabled = false;
            notifyAll();
        }

        public synchronized void enable() {
            enabled = true;
        }



        public synchronized void waitDisabled() {
            try {
                while (enabled) {
                    wait();
                }
            } catch (InterruptedException e) {
                LOGGER.warning("FrameBufferHA interrupted");
            }
        }

        public boolean isEnabled() {
            return enabled;
        }
    }


    private static class BitmapRequest {
        private final GraphicFactory factory;
        private final Dimension dimension;
        private final boolean transparent;
        private final int color;


        public BitmapRequest(GraphicFactory f, Dimension d, int c, boolean t) {
            factory = f;
            dimension = d;
            transparent = t;
            color = c;
        }

        public Bitmap create() {
            if (dimension.width > 0 && dimension.height > 0) {
                Bitmap b = factory.createBitmap(
                        dimension.width,
                        dimension.height,
                        transparent);

                b.setBackgroundColor(color);
                return b;
            }
            return null;
        }
    }
}
