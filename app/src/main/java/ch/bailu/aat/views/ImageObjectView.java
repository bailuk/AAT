package ch.bailu.aat.views;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjBitmap;
import ch.bailu.aat.services.cache.ObjImageAbstract;
import ch.bailu.aat.services.cache.Obj;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;

public class ImageObjectView extends ImageView {

    private boolean isAttached=false;

    protected final ServiceContext scontext;

    private ObjImageAbstract imageHandle= ObjBitmap.NULL;

    private String idToLoad=null;
    private Obj.Factory factoryToLoad=null;


    private final int defaultImageID;

    public ImageObjectView(ServiceContext sc, int resID) {
        super(sc.getContext());
        scontext = sc;
        defaultImageID = resID;

        resetImage();
    }


    public void setImageObject() {
        idToLoad = null;
        factoryToLoad = null;

        resetImage();
    }



    public void setImageObject(String ID, Obj.Factory factory) {
        idToLoad = ID;
        factoryToLoad=factory;

        loadAndDisplayImage();
    }


    private void loadAndDisplayImage() {
        if (idToLoad != null && isAttached) {

            freeImageHandle();

            if (loadImage(idToLoad, factoryToLoad)) {
                displayImage();
            } else {
                resetImage();
            }

            idToLoad = null;
            factoryToLoad = null;
        }
    }

    private void resetImage() {
        if (defaultImageID != 0) setImageResource(defaultImageID);
        else setImageDrawable(null);
    }


    private boolean loadImage(final String id, final Obj.Factory factory) {


        final boolean[] r = {false};
        new InsideContext(scontext) {
            @Override
            public void run() {
                final Obj h=scontext.getCacheService().getObject(id, factory);

                if (h instanceof ObjImageAbstract)
                {
                    imageHandle = (ObjImageAbstract) h;
                    r[0] = true;
                } else {
                    h.free();
                }


            }
        };

        return r[0];
    }


    private void freeImageHandle() {
        imageHandle.free();
        imageHandle = ObjBitmap.NULL;
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
        if (imageHandle.hasException()) {
            resetImage();
        } else if (imageHandle.isReadyAndLoaded()) {
            setImageBitmap(imageHandle.getBitmap());
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
