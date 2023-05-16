 package ch.bailu.aat_lib.dispatcher;


import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.service.ServicesInterface;

public class TrackerSource extends ContentSource {

    private final Broadcaster broadcaster;
    private final ServicesInterface services;

    public TrackerSource (ServicesInterface services, Broadcaster broadcaster) {
        this.broadcaster = broadcaster;
        this.services = services;

    }

    private final BroadcastReceiver onTrackChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(String... args) {
            sendUpdate(InfoID.TRACKER, services.getTrackerService().getInfo());
        }
    };



    @Override
    public void requestUpdate() {
        sendUpdate(InfoID.TRACKER, services.getTrackerService().getInfo());
    }


    @Override
    public void onPause() {
        broadcaster.unregister(onTrackChanged);
    }


    @Override
    public void onResume() {
        broadcaster.register(onTrackChanged, AppBroadcaster.TRACKER);
    }

    @Override
    public int getIID() {
        return InfoID.TRACKER;
    }

    @Override
    public GpxInformation getInfo() {
        return services.getTrackerService().getInfo();
    }
}
