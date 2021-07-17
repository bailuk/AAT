package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.Closeable;

import ch.bailu.aat.preferences.map.SolidOverlayFile;
import ch.bailu.aat.preferences.map.SolidOverlayFileList;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.Obj;
import ch.bailu.aat.services.cache.ObjGpx;
import ch.bailu.aat.services.cache.ObjGpxStatic;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;


public class OverlaySource extends ContentSource {



    private final ServiceContext scontext;

    private final OverlayInformation[] overlays = new OverlayInformation[SolidOverlayFileList.MAX_OVERLAYS];

    public OverlaySource(ServiceContext sc) {
        scontext=sc;
    }


    @Override
    public void onPause() {
        for (OverlayInformation o : overlays) o.close();
    }




    @Override
    public void onResume() {
        for (int i=0; i<overlays.length; i++)
            overlays[i]= new OverlayInformation(i);
    }

    @Override
    public int getIID() {
        return InfoID.OVERLAY;
    }

    @Override
    public GpxInformation getInfo() {
        return GpxInformation.NULL;
    }


    @Override
    public void requestUpdate() {
        for (OverlayInformation o: overlays) o.initAndUpdateOverlay();
    }



    private class OverlayInformation extends GpxInformation implements Closeable {
        private final int infoID;

        private final SolidOverlayFile soverlay;

        private ObjGpx handle = ObjGpxStatic.NULL;
        private BoundingBoxE6 bounding = BoundingBoxE6.NULL_BOX;


        public OverlayInformation(int index) {
            infoID = InfoID.OVERLAY+index;

            soverlay = new SolidOverlayFile(scontext.getContext(), index);
            soverlay.register(onPreferencesChanged);
            OldAppBroadcaster.register(scontext.getContext(), onFileProcessed, AppBroadcaster.FILE_CHANGED_INCACHE);
        }


        private final BroadcastReceiver  onFileProcessed = new BroadcastReceiver () {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (AppIntent.hasFile(intent, handle.toString())) {
                    initAndUpdateOverlay();
                }
            }

        };

        private final OnPreferencesChanged onPreferencesChanged = new OnPreferencesChanged() {
            @Override
            public void onPreferencesChanged(StorageInterface s, String key) {
                if (soverlay.hasKey(key)) {
                    initAndUpdateOverlay();
                }
            }
        };


        private void disableOverlay() {
            handle.free();
            handle = ObjGpxStatic.NULL;
            bounding = BoundingBoxE6.NULL_BOX;
        }




        public void initAndUpdateOverlay() {

            if (soverlay.isEnabled()) {
                Foc file = soverlay.getValueAsFile();
                enableOverlay(file.getPath());

            } else {
                disableOverlay();
            }
            sendUpdate(infoID, this);
        }


        private void enableOverlay(String fileId) {
            final Obj oldHandle = handle;

            handle = getObjectSave(fileId);
            oldHandle.free();
            setBounding();
        }


        private ObjGpx getObjectSave(String id) {
            Obj h = scontext.getCacheService().getObject(id, new ObjGpxStatic.Factory());
            if (ObjGpx.class.isInstance(h)==false) {
                h= ObjGpx.NULL;
            }
            return (ObjGpx)h;
        }

        private void setBounding() {
            GpxList list = handle.getGpxList();
            bounding = list.getDelta().getBoundingBox();
        }


        @Override
        public GpxType getType() {
            if (isLoaded() && getGpxList() != null && getGpxList().getDelta() != null) {
                return getGpxList().getDelta().getType();
            }
            return GpxType.NONE;
        }

        @Override
        public Foc getFile() {
            return FocAndroid.factory(scontext.getContext(),handle.toString());
        }


        @Override
        public GpxList getGpxList() {
            return handle.getGpxList();
        }

        @Override
        public BoundingBoxE6 getBoundingBox() {
            return bounding;
        }

        @Override
        public boolean isLoaded() {
            return handle.isReadyAndLoaded();// && handle.getGpxList().getPointList().size()>0;
        }

        @Override
        public void close() {
            handle.free();
            handle = ObjGpxStatic.NULL;
            soverlay.unregister(onPreferencesChanged);
            scontext.getContext().unregisterReceiver(onFileProcessed);
        }
    }
}
