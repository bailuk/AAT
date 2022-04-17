package ch.bailu.aat_lib.dispatcher;

import java.io.Closeable;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidOverlayFile;
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjGpx;
import ch.bailu.aat_lib.service.cache.ObjGpxStatic;
import ch.bailu.foc.Foc;


public class OverlaySource extends ContentSource {

    private final AppContext appContext;

    private final OverlayInformation[] overlays = new OverlayInformation[SolidOverlayFileList.MAX_OVERLAYS];

    public OverlaySource(AppContext sc) {
        appContext =sc;
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

            soverlay = new SolidOverlayFile(appContext.getStorage(), appContext, index);
            soverlay.register(onPreferencesChanged);
            appContext.getBroadcaster().register(onFileProcessed, AppBroadcaster.FILE_CHANGED_INCACHE);
        }


        private final BroadcastReceiver onFileProcessed = args -> {
            if (BroadcastData.has(args, handle.toString())) {
                initAndUpdateOverlay();
            }
        };

        private final OnPreferencesChanged onPreferencesChanged = new OnPreferencesChanged() {
            @Override
            public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {
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
            Obj handler = appContext.getServices().getCacheService().getObject(id, new ObjGpxStatic.Factory());
            if (!(handler instanceof ObjGpx)) {
                handler = ObjGpx.NULL;
            }
            return (ObjGpx)handler;
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
            return appContext.toFoc(handle.toString());
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
            appContext.getBroadcaster().unregister(onFileProcessed);
        }
    }
}
