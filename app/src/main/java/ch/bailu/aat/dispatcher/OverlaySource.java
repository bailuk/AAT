package ch.bailu.aat.dispatcher;

import java.io.Closeable;
import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppIntent;
import ch.bailu.aat.preferences.SolidOverlayFile;
import ch.bailu.aat.preferences.SolidOverlayFileList;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;
import ch.bailu.aat.services.cache.ObjectHandle;


public class OverlaySource extends ContentSource {
    public static final int MAX_OVERLAYS=SolidOverlayFileList.MAX_OVERLAYS;


    private final ServiceContext scontext;

    private final OverlayInformation[] overlayList = new OverlayInformation[MAX_OVERLAYS];

    public OverlaySource(ServiceContext sc) {
        scontext=sc;
    }	


    @Override
    public void onPause() {
        for (OverlayInformation anOverlayList : overlayList) anOverlayList.close();
    }




    @Override
    public void onResume() {
        for (int i=0; i<MAX_OVERLAYS; i++)
            overlayList[i]= new OverlayInformation(GpxInformation.ID.INFO_ID_OVERLAY+i);
    }


    @Override
    public void close() {}


    @Override
    public void forceUpdate() {
        for (int i=0; i<MAX_OVERLAYS; i++) 
            overlayList[i].initAndUpdateOverlay();
    }



    private class OverlayInformation extends GpxInformation implements Closeable {
        private final int updateID;

        private final SolidOverlayFile soverlay;

        private GpxObject handle = GpxObjectStatic.NULL;
        private BoundingBox bounding = BoundingBox.NULL_BOX;


        public OverlayInformation(int id) {

            updateID = id;

            soverlay = new SolidOverlayFile(scontext.getContext(), id-ID.INFO_ID_OVERLAY);
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
            bounding = BoundingBox.NULL_BOX;
        }




        public void initAndUpdateOverlay() {

            if (soverlay.isEnabled()) {
                File file = soverlay.getFile();
                enableOverlay(file.getAbsolutePath());

            } else {
                disableOverlay();
            }
            updateGpxContent(this);
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
            GpxList list = ((GpxObject)handle).getGpxList();

            return list;
        }

        @Override
        public BoundingBox getBoundingBox() {
            return bounding;
        }

        @Override
        public boolean isLoaded() {
            return handle.isReady() && handle.getGpxList().getPointList().size()>0;
        }


        @Override 
        public int getID() {
            return updateID;
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
