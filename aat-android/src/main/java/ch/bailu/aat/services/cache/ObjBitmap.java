package ch.bailu.aat.services.cache;

import org.mapsforge.core.graphics.Bitmap;

import ch.bailu.aat.util.graphic.SyncBitmap;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.background.FileTask;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.OnObject;
import ch.bailu.aat_lib.service.cache.icons.ObjImageAbstract;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocName;

public final class ObjBitmap extends ObjImageAbstract {
    public final static ObjBitmap NULL=new ObjBitmap();

    private final SyncBitmap bitmap=new SyncBitmap();
    private final Foc imageFile;


    private ObjBitmap() {
        this(new FocName(""));
    }

    public ObjBitmap(Foc id) {
        super(id.getPath());
        imageFile = id;
    }


    @Override
    public void onInsert(AppContext sc){
        load(sc.getServices());

        sc.getServices().getCacheService().addToBroadcaster(this);
    }


    @Override
    public void onRemove(AppContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }



    private void load(ServicesInterface sc) {
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
        public Obj factory(String id, AppContext sc) {
            return new ObjBitmap(sc.toFoc(id));
        }
    }


    @Override
    public void onDownloaded(String id, String url, AppContext sc) {
        if (id.equals(toString())) {
            load(sc.getServices());
        }
    }


    @Override
    public void onChanged(String id, AppContext sc) {}



    private static class BitmapLoader extends FileTask {

        public BitmapLoader(Foc f) {
            super(f);
        }

        @Override
        public long bgOnProcess(final AppContext appContext) {
            final long[] size = {0};

            new OnObject(appContext, toString(), ObjBitmap.class) {
                @Override
                public void run(Obj obj) {
                    ObjBitmap self = (ObjBitmap) obj;

                    try {
                        self.bitmap.set(self.imageFile);
                        size[0] =  self.bitmap.getSize();
                    } catch (Exception e) {
                        self.setException(e);
                    }

                    appContext.getBroadcaster().broadcast(AppBroadcaster.FILE_CHANGED_INCACHE,
                            self.imageFile.getPath());

                }
            };
            return size[0];
        }
    }
}
