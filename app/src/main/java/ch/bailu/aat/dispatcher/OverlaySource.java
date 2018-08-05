package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import java.io.Closeable;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.preferences.SolidOverlayFile;
import ch.bailu.aat.preferences.SolidOverlayFileList;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.util_java.foc.Foc;


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

        private GpxObject handle = GpxObjectStatic.NULL;
        private BoundingBoxE6 bounding = BoundingBoxE6.NULL_BOX;


        public OverlayInformation(int index) {
            infoID = InfoID.OVERLAY+index;

            soverlay = new SolidOverlayFile(scontext.getContext(), index);
            soverlay.register(onPreferencesChanged);
            AppBroadcaster.register(scontext.getContext(), onFileProcessed, AppBroadcaster.FILE_CHANGED_INCACHE);
        }


        private final BroadcastReceiver  onFileProcessed = new BroadcastReceiver () {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (AppIntent.hasFile(intent, handle.toString())) {
                    initAndUpdateOverlay();
                }
            }

        };

        private final OnSharedPreferenceChangeListener onPreferencesChanged = new OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
                if (soverlay.hasKey(key)) {
                    initAndUpdateOverlay();
                }
            }       
        };


        private void disableOverlay() {
            handle.free();
            handle = GpxObjectStatic.NULL;
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
            final ObjectHandle oldHandle = handle;

            handle = getObjectSave(fileId);
            oldHandle.free();
            setBounding();
        }


        private GpxObject getObjectSave(String id) {
            ObjectHandle h = scontext.getCacheService().getObject(id, new GpxObjectStatic.Factory());
            if (GpxObject.class.isInstance(h)==false) {
                h=GpxObject.NULL;
            }
            return (GpxObject)h;
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
            return handle.isReadyAndLoaded() && handle.getGpxList().getPointList().size()>0;
        }

        @Override
        public void close() {
            handle.free();
            handle = GpxObjectStatic.NULL;
            soverlay.unregister(onPreferencesChanged);
            scontext.getContext().unregisterReceiver(onFileProcessed);
        }
    }
}
