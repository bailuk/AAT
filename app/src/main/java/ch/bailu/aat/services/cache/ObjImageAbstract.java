package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

public abstract class ObjImageAbstract extends Obj {
    public ObjImageAbstract(String id) {
        super(id);
    }


    public abstract Bitmap getBitmap();
}
