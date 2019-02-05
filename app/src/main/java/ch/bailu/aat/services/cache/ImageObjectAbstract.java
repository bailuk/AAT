package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

public abstract class ImageObjectAbstract extends ObjectHandle {
    public ImageObjectAbstract(String id) {
        super(id);
    }


    public abstract Bitmap getBitmap();
}
