package ch.bailu.aat.map.mapsforge;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.util.ui.AppLog;

public class FrameBufferBitmap {

    private final Lock frameLock = new Lock();
    private final Lock allowSwap = new Lock();

    private Bitmap bitmap = null;


    public Bitmap lock() {
        synchronized (frameLock) {
            if (bitmap != null) {
                frameLock.enable();
            }
            return bitmap;
        }
    }

    public Bitmap lockWhenSwapped() {
        allowSwap.waitDisabled();
        return lock();
    }




    public void release() {
        frameLock.disable();
        synchronized(frameLock) {
            if (bitmap != null)
                allowSwap.enable();
        }
    }

    public void create(int w, int h) {
        bitmap = AndroidGraphicFactory.INSTANCE.createBitmap(w, h, false);
    }

    public void destroy()  {

        synchronized(frameLock) {
            if (bitmap != null) {
                AppLog.d(this, "destroy frame");
                frameLock.waitDisabled();

                bitmap.decrementRefCount();
                bitmap = null;
            }
        }
        allowSwap.disable();
    }

    public static boolean swap(FrameBufferBitmap a, FrameBufferBitmap b) {
        if (a.allowSwap.isEnabled()) {
            if (b.allowSwap.isEnabled()) {
                Bitmap t = a.bitmap;
                a.bitmap = b.bitmap;
                b.bitmap = t;

                a.allowSwap.disable();
                b.allowSwap.disable();
                return true;
            }
        }
        return false;
    }


    private static class Lock {
        private boolean enabled =false;


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
                e.printStackTrace();
            }
        }

        public boolean isEnabled() {
            return enabled;
        }
    }
}
