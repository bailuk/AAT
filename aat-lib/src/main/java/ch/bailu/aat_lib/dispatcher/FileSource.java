package ch.bailu.aat_lib.dispatcher;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.cache.gpx.GpxHandler;
import ch.bailu.foc.Foc;

public class FileSource extends ContentSource implements FileSourceInterface {
    private final AppContext context;
    private final int iid;

    private final GpxHandler gpxHandler = new GpxHandler();

    private boolean lifeCycleEnabled = false;
    private boolean trackEnabled = true;

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
        lifeCycleEnabled = false;
        context.getBroadcaster().unregister(onChangedInCache);
        gpxHandler.disable();
    }


    @Override
    public void onResume() {
        lifeCycleEnabled = true;
        context.getBroadcaster().register(onChangedInCache, AppBroadcaster.FILE_CHANGED_INCACHE);

        if (trackEnabled) {
            gpxHandler.enable(context.getServices());
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
    public void setFile(Foc file) {
        gpxHandler.setFileID(context.getServices(), file);
        requestUpdate();
    }

    @Override
    public boolean isEnabled() {
        return lifeCycleEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.trackEnabled = enabled;

        if (lifeCycleEnabled && trackEnabled) {
            gpxHandler.enable(context.getServices());
        } else {
            gpxHandler.disable();
        }
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
