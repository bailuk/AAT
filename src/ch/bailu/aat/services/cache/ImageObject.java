package ch.bailu.aat.services.cache;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.services.cache.CacheService.SelfOn;

public class ImageObject extends ObjectHandle {
    public final static ImageObject NULL=new ImageObject();
    
    private final SynchronizedBitmap bitmap=new SynchronizedBitmap();


    private ImageObject() {
        super("");
    }
    
    public ImageObject(String id, SelfOn self) {
        super(id);
    }
    
    @Override
    public void onInsert(SelfOn self){
        load(self);
        
        self.broadcaster.put(this);
    }


    
    private void load(SelfOn self) {
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
        self.background.load(l);
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
        public ObjectHandle factory(String id, SelfOn self) {
            return new ImageObject(id, self);
        }
    }


    @Override
    public void onDownloaded(String id, String url, SelfOn self) {
        if (id.equals(toString())) {
            load(self);
        }
    }

    
    @Override
    public void onChanged(String id, SelfOn self) {}
}
