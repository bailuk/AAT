package ch.bailu.aat_lib.dispatcher;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.GpxFileWrapper;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjGpx;
import ch.bailu.aat_lib.service.cache.ObjGpxStatic;
import ch.bailu.aat_lib.service.cache.ObjNull;

public class CustomFileSource extends ContentSource {
    private final AppContext appContext;

    private Obj handle = ObjNull.NULL;
    private String fileID;


    private final BroadcastReceiver onChangedInCache = args -> {
        if (BroadcastData.has(args, fileID)) {
            requestUpdate();
        }
    };


    public CustomFileSource(AppContext appContext) {
        this(appContext, "");
    }

    public CustomFileSource(AppContext appContext, String fileID) {
        this.appContext = appContext;
        this.fileID = fileID;
    }


    public void setFileID(String fileID) {
        this.fileID = fileID;
        requestUpdate();
    }


    @Override
    public void requestUpdate() {
        new InsideContext(appContext.getServices()) {
            @Override
            public void run() {
                Obj h = ObjNull.NULL;
                if (! "".equals(fileID)) {
                    h = appContext.getServices().getCacheService().getObject(fileID, new ObjGpxStatic.Factory());
                }

                handle.free();
                handle = h;

                if (h instanceof ObjGpx && h.isReadyAndLoaded()) {
                    sendUpdate(InfoID.FILEVIEW, new GpxFileWrapper(h.getFile(), ((ObjGpx) h).getGpxList()));
                }
            }
        };
    }


    @Override
    public void onPause() {
        appContext.getBroadcaster().unregister(onChangedInCache);
        handle.free();
        handle = ObjNull.NULL;
    }


    @Override
    public void onResume() {
        appContext.getBroadcaster().register(onChangedInCache, AppBroadcaster.FILE_CHANGED_INCACHE);
    }

    @Override
    public int getIID() {
        return InfoID.FILEVIEW;
    }

    @Override
    public GpxInformation getInfo() {
        return GpxInformation.NULL;
    }
}
