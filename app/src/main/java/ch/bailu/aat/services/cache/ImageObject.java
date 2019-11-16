package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

import java.io.IOException;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileTask;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.graphic.SyncBitmap;
import ch.bailu.util_java.foc.Foc;

public class ImageObject extends ImageObjectAbstract {
    public final static ImageObject NULL=new ImageObject();

    private final SyncBitmap bitmap=new SyncBitmap();
    private final Foc imageFile;


    private ImageObject() {
        this(FocAndroid.NULL);
    }

    public ImageObject(Foc id) {
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


    public boolean isReadyAndLoaded() {
        return getBitmap() != null;
    }

    @Override
    public synchronized Bitmap getBitmap() {
        return bitmap.getAndroidBitmap();
    }


    public static class Factory extends ObjectHandle.Factory {
        @Override
        public ObjectHandle factory(String id, ServiceContext sc) {
            return new ImageObject(FocAndroid.factory(sc.getContext(),id));
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

            new OnObject(sc, toString(), ImageObject.class) {
                @Override
                public void run(ObjectHandle obj) {
                    ImageObject self = (ImageObject) obj;

                    try {
                        self.bitmap.set(self.imageFile);
                        size[0] =  self.bitmap.getSize();
                    } catch (IOException e) {
                        self.setException(e);
                    }

                    AppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_INCACHE,
                            self.imageFile);

                }
            };
            return size[0];
        }

    }


}
