package ch.bailu.aat.services.cache;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.graphic.SyncBitmap;
import ch.bailu.simpleio.foc.Foc;

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



    private void load(final ServiceContext sc) {
        FileHandle l=new FileHandle(imageFile) {

            @Override
            public long bgOnProcess() {
                long size = 0;

                if (sc.lock()) {
                    ObjectHandle handle = sc.getCacheService().getObject(toString());

                    if (handle != null) {

                        if (handle instanceof ImageObject) {
                            ImageObject self = (ImageObject) handle;

                            bitmap.set(self.imageFile);
                            size =  bitmap.getSize();
                        }
                        handle.free();
                    }

                    sc.free();
                }
                return size;
            }

            @Override
            public void broadcast(Context context) {
                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, ImageObject.this.toString());
            }

        };
        sc.getBackgroundService().load(l);
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

    @Override
    public synchronized Drawable getDrawable(Resources res) {
        return bitmap.getDrawable(res);
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
}
