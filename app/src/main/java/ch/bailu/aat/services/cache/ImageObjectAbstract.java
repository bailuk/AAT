package ch.bailu.aat.services.cache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public abstract class ImageObjectAbstract extends ObjectHandle {
    public ImageObjectAbstract(String id) {
        super(id);
    }


    public abstract Bitmap getBitmap();

    public abstract Drawable getDrawable(Resources res);

}
