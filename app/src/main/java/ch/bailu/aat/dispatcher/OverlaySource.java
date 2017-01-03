package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import java.io.Closeable;
import java.io.File;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.preferences.SolidOverlayFile;
import ch.bailu.aat.preferences.SolidOverlayFileList;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;
import ch.bailu.aat.services.cache.ObjectHandle;


public class OverlaySource extends ContentSource {
    public static final int MAX_OVERLAYS=SolidOverlayFileList.MAX_OVERLAYS;


    private final ServiceContext scontext;

    private final OverlayInformation[] overlays = new OverlayInformation[MAX_OVERLAYS];

    public OverlaySource(ServiceContext sc) {
        scontext=sc;
    }	


    @Override
    public void onPause() {
        for (OverlayInformation o : overlays) o.close();
    }




    @Override
    public void onResume() {
        for (int i=0; i<MAX_OVERLAYS; i++)
            overlays[i]= new OverlayInformation(i);
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
                File file = soverlay.toFile();
                enableOverlay(file.getAbsolutePath());

            } else {
                disableOverlay();
            }
            //AppLog.d(this, "send " + infoID);
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
        public String getName() {
            return new File(handle.toString()).getName();
        }

        @Override
        public String getPath() {
            return handle.toString();
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
            return handle.isReady() && handle.getGpxList().getPointList().size()>0;
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
