package ch.bailu.aat.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import java.io.File;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ImageObject;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.simpleio.foc.Foc;
import ch.bailu.simpleio.foc.FocFile;

public class PreviewView extends ImageView {
    private boolean isAttached=false;

    private final ServiceContext scontext;

    private ImageObject imageHandle=ImageObject.NULL;
    private Foc        imageToLoad=null;
    
    public PreviewView(ServiceContext sc) {
        super(sc.getContext());
        scontext = sc;
    }
    
    
    
    public void setFilePath(String fileID) {
        final Foc file = AppDirectory.getPreviewFile(new FocFile(new File(fileID)));
        setPreviewPath(file);
        loadAndDisplayImage();
    }
    
    

    public void setPreviewPath(Foc file) {
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


    private boolean loadImage(Foc f) {
        if (f.canRead()) {
            final ObjectHandle  h=scontext.getCacheService().getObject(f.toString(), new ImageObject.Factory());

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
        if (imageHandle.isReadyAndLoaded()) {
            setImageDrawable(imageHandle.getDrawable(getResources()));
        }
    }


    private final BroadcastReceiver onFileChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String file = imageHandle.toString();

            if (AppIntent.hasFile(intent, file)) displayImage();
        }
    };
}
