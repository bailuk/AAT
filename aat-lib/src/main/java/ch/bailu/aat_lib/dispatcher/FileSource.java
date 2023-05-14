package ch.bailu.aat_lib.dispatcher;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.GpxFileWrapper;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjGpx;
import ch.bailu.aat_lib.service.cache.ObjGpxStatic;
import ch.bailu.aat_lib.service.cache.ObjNull;

public class FileSource extends ContentSource implements FileSourceInterface {
    private final AppContext context;

    private final int iid;

    private Obj handle = ObjNull.NULL;
    private String fileID = "";

    public FileSource(AppContext context, int iid) {
        this.context = context;
        this.iid = iid;
    }

    private final BroadcastReceiver onChangedInCache = args -> {
        if (BroadcastData.has(args, fileID)) {
            requestUpdate();
        }
    };

    @Override
    public void onPause() {
        context.getBroadcaster().unregister(onChangedInCache);
        handle.free();
        handle = ObjNull.NULL;
    }


    @Override
    public void onResume() {
        context.getBroadcaster().register(onChangedInCache, AppBroadcaster.FILE_CHANGED_INCACHE);
    }

    @Override
    public int getIID() {
        return iid;
    }

    @Override
    public GpxInformation getInfo() {
        return new GpxFileWrapper(handle.getFile(), getList());
    }

    @Override
    public void setFileID(String fileID) {
        this.fileID = fileID;
        requestUpdate();
    }

    @Override
    public void requestUpdate() {
        new InsideContext(context.getServices()) {
            @Override
            public void run() {
                Obj h = ObjNull.NULL;
                if (!"".equals(fileID)) {
                    h = context.getServices().getCacheService().getObject(fileID, new ObjGpxStatic.Factory());
                }

                handle.free();
                handle = h;
                sendUpdate(iid, getInfo());
            }
        };
    }

    private GpxList getList() {
        if (handle instanceof ObjGpx && handle.isReadyAndLoaded()) {
            return ((ObjGpx) handle).getGpxList();
        } else {
            return GpxList.NULL_ROUTE;
        }
    }
}
