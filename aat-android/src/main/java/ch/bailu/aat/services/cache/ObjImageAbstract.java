package ch.bailu.aat.services.cache;


import org.mapsforge.core.graphics.Bitmap;

public abstract class ObjImageAbstract extends Obj {
    public ObjImageAbstract(String id) {
        super(id);
    }


    public abstract Bitmap getBitmap();
}
