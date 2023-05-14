package ch.bailu.aat_lib.dispatcher;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.cache.gpx.GpxHandler;

public class FileSource extends ContentSource implements FileSourceInterface {
    private final AppContext context;
    private final int iid;

    private final GpxHandler gpxHandler = new GpxHandler();

    private boolean enabled = false;

    public FileSource(AppContext context, int iid) {
        this.context = context;
        this.iid = iid;
    }

    private final BroadcastReceiver onChangedInCache = args -> {
        if (BroadcastData.has(args, gpxHandler.get().getID())) {
            requestUpdate();
        }
    };

    @Override
    public void onPause() {
        context.getBroadcaster().unregister(onChangedInCache);
        gpxHandler.disable();
    }


    @Override
    public void onResume() {
        context.getBroadcaster().register(onChangedInCache, AppBroadcaster.FILE_CHANGED_INCACHE);

        if (enabled) {
            gpxHandler.enable(context.getServices().getCacheService());
        }
    }

    @Override
    public int getIID() {
        return iid;
    }

    @Override
    public GpxInformation getInfo() {
        return gpxHandler.getInfo();
    }

    @Override
    public void setFileID(String fileID) {
        gpxHandler.setFileID(context.getServices().getCacheService(), fileID);
        requestUpdate();
    }

    @Override
    public void enable() {
        enabled = true;
        gpxHandler.enable(context.getServices().getCacheService());
        requestUpdate();
    }

    @Override
    public void disable() {
        enabled = false;
        gpxHandler.disable();
        requestUpdate();
    }

    @Override
    public void requestUpdate() {
        new InsideContext(context.getServices()) {
            @Override
            public void run() {
                sendUpdate(iid, getInfo());
            }
        };
    }
}
