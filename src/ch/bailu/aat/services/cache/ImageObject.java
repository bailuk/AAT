package ch.bailu.aat.services.cache;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.MultiServiceLink.ServiceContext;
import ch.bailu.aat.services.background.FileHandle;

public class ImageObject extends ObjectHandle {
    public final static ImageObject NULL=new ImageObject();
    
    private final SynchronizedBitmap bitmap=new SynchronizedBitmap();


    private ImageObject() {
        super("");
    }
    
    public ImageObject(String id, ServiceContext sc) {
        super(id);
    }
    
    @Override
    public void onInsert(ServiceContext sc){
        load(sc);
        
        sc.getCacheService().addToBroadcaster(this);
    }


    
    private void load(ServiceContext sc) {
        FileHandle l=new FileHandle(toString()) {

            @Override
            public long bgOnProcess() {
                File file = new File(toString());
                if (file.exists())
                    bitmap.load(toString());
                return bitmap.getSize();
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

    
    @Override
    public boolean isReady() {
        return bitmap.getDrawable() != null;
    }

    public synchronized Bitmap getBitmap() {
        return bitmap.get();
    }

    public synchronized Drawable getDrawable() {
        return bitmap.getDrawable();
    }


    public static class Factory extends ObjectHandle.Factory {
        @Override
        public ObjectHandle factory(String id, ServiceContext sc) {
            return new ImageObject(id, sc);
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
