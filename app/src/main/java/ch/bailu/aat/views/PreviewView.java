package ch.bailu.aat.views;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppIntent;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ImageObject;
import ch.bailu.aat.services.cache.ObjectHandle;

public class PreviewView extends ImageView {
    private boolean isAttached=false;

    private final ServiceContext scontext;

    private ImageObject imageHandle=ImageObject.NULL;
    private File        imageToLoad=null;
    
    public PreviewView(ServiceContext sc) {
        super(sc.getContext());
        scontext = sc;
    }
    
    
    
    public void setFilePath(String fileID) {
        final File file = AppDirectory.getPreviewFile(new File(fileID));
        setPreviewPath(file);
        loadAndDisplayImage();
    }
    
    

    public void setPreviewPath(File file) {
        imageToLoad = file;
        loadAndDisplayImage();
    }

    
    private void loadAndDisplayImage() {
        if (imageToLoad != null && isAttached) {

            freeImageHandle();

            if (loadImage(imageToLoad)) {
                displayImage();
            } else {
                setImageDrawable(null);
            }
            
            imageToLoad=null;
        }
    }


    private boolean loadImage(File f) {
        if (f.exists()) {
            final ObjectHandle  h=scontext.getCacheService().getObject(f.getAbsolutePath(), new ImageObject.Factory());

            if (ImageObject.class.isInstance(h)) {
                imageHandle = (ImageObject) h;
                return true;
            } else {
                h.free();
            }
        }
        return false;
    }

    
    private void freeImageHandle() {
        imageHandle.free();
        imageHandle = ImageObject.NULL;
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached=true;

        AppBroadcaster.register(getContext(), 
                onFileChanged, 
                AppBroadcaster.FILE_CHANGED_INCACHE);
        loadAndDisplayImage();
    }


    @Override
    public void onDetachedFromWindow() {
        getContext().unregisterReceiver(onFileChanged);
        freeImageHandle();

        isAttached=false;
        super.onDetachedFromWindow();
    }


    private void displayImage() {
        if (imageHandle.isReady()) { 
            setImageDrawable(imageHandle.getDrawable());
        }
    }


    private BroadcastReceiver onFileChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String file = imageHandle.toString();

            if (AppIntent.hasFile(intent, file)) displayImage();
        }
    };
}
