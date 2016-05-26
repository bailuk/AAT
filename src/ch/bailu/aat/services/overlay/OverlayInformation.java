package ch.bailu.aat.services.overlay;

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
import ch.bailu.aat.preferences.SolidOverlayFile;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;
import ch.bailu.aat.services.cache.ObjectHandle;

public class OverlayInformation extends GpxInformation implements Closeable {
        private final int updateID;

        private final SolidOverlayFile soverlay;
        private final ServiceContext serviceContext;

        private GpxObject handle = GpxObjectStatic.NULL;
        private BoundingBox bounding = BoundingBox.NULL_BOX;


        public OverlayInformation(int id, ServiceContext sc) {
            serviceContext = sc;
            updateID = id;

            soverlay = new SolidOverlayFile(serviceContext.getContext(), id-ID.INFO_ID_OVERLAY);
            soverlay.register(onPreferencesChanged);
            AppBroadcaster.register(serviceContext.getContext(), onFileProcessed, AppBroadcaster.FILE_CHANGED_INCACHE);
            
            initOverlay();
        }


        private final BroadcastReceiver  onFileProcessed = new BroadcastReceiver () {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (AppBroadcaster.hasFile(intent, handle.toString())) {
                    initOverlay();
                }
            }

        };

        private final OnSharedPreferenceChangeListener onPreferencesChanged = new OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                    String key) {

                initOverlay();
            }       
        };

        
        private void disableOverlay() {
            handle.free();
            handle = GpxObjectStatic.NULL;
            bounding = BoundingBox.NULL_BOX;
        }




        private void initOverlay() {

            if (soverlay.isEnabled()) {
                File file = soverlay.getFile();
                enableOverlay(file.getAbsolutePath());
                
            } else {
                disableOverlay();
            }
            AppBroadcaster.broadcast(serviceContext.getContext(), AppBroadcaster.OVERLAY_CHANGED, updateID);
        }


        private void enableOverlay(String fileId) {
            final ObjectHandle oldHandle = handle;

            handle = (GpxObject)serviceContext.getCacheService().getObject(fileId, new GpxObjectStatic.Factory());
            oldHandle.free();
            setBounding();
        }

        
        private void setBounding() {
            GpxList list = ((GpxObject)handle).getGpxList();
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
            serviceContext.getContext().unregisterReceiver(onFileProcessed);
        }
}
