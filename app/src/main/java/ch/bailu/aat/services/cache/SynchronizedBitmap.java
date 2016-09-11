package ch.bailu.aat.services.cache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class SynchronizedBitmap {
        private Bitmap bitmap=null;
        private Drawable drawable = null;
        
        private long size=ObjectHandle.MIN_SIZE;
        
        
        public synchronized Bitmap get() {
            return bitmap;
        }
        
        
        public synchronized Drawable getDrawable() {
            return drawable;
        }
        
        
        public boolean load(String file, Bitmap def) {
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

        
        public synchronized long getSize() {
            return size;
        }
        
        @SuppressWarnings("deprecation")
        public synchronized void set(Bitmap b) {
            if (b != null) {

                bitmap = b;
                drawable = new BitmapDrawable(b);
                size=b.getHeight()*b.getRowBytes();
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
