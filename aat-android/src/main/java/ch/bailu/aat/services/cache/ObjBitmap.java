package ch.bailu.aat.services.cache;

import org.mapsforge.core.graphics.Bitmap;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.service.background.FileTask;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat.util.graphic.SyncBitmap;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;

public final class ObjBitmap extends ObjImageAbstract {
    public final static ObjBitmap NULL=new ObjBitmap();

    private final SyncBitmap bitmap=new SyncBitmap();
    private final Foc imageFile;


    private ObjBitmap() {
        this(FocAndroid.NULL);
    }

    public ObjBitmap(Foc id) {
        super(id.getPath());
        imageFile = id;
    }




    @Override
    public void onInsert(ServiceContext sc){
        load(sc);

        sc.getCacheService().addToBroadcaster(this);
    }


    public void onRemove(ServiceContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }



    private void load(ServiceContext sc) {
        sc.getBackgroundService().process(new BitmapLoader(imageFile));
    }



    @Override
    public long getSize() {
        return bitmap.getSize();
    }

    @Override
    public boolean isReadyAndLoaded() {
        return getBitmap() != null;
    }

    @Override
    public synchronized Bitmap getBitmap() {
        return bitmap.getBitmap();
    }


    public static class Factory extends Obj.Factory {
        @Override
        public Obj factory(String id, ServiceContext sc) {
            return new ObjBitmap(FocAndroid.factory(sc.getContext(),id));
        }
    }


    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {
        if (id.equals(toString())) {
            load(sc);
        }
    }


    @Override
    public void onChanged(String id, ServiceContext sc) {}



    private static class BitmapLoader extends FileTask {

        public BitmapLoader(Foc f) {
            super(f);
        }

        @Override
        public long bgOnProcess(final ServiceContext sc) {
            final long[] size = {0};

            new OnObject(sc, toString(), ObjBitmap.class) {
                @Override
                public void run(Obj obj) {
                    ObjBitmap self = (ObjBitmap) obj;

                    try {
                        self.bitmap.set(self.imageFile);
                        size[0] =  self.bitmap.getSize();
                    } catch (Exception e) {
                        self.setException(e);
                    }

                    OldAppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_INCACHE,
                            self.imageFile);

                }
            };
            return size[0];
        }

    }


}
