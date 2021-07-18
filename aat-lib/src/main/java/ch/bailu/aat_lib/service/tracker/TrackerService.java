package ch.bailu.aat_lib.service.tracker;

import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.VirtualService;
import ch.bailu.aat_lib.util.WithStatusText;

public final class TrackerService extends VirtualService implements WithStatusText, TrackerServiceInterface {

    private final TrackerInternals internal;
    private final Broadcaster broadcaster;


    public TrackerService(SolidDataDirectory sdirectory, StatusIconInterface statusIconInterface, Broadcaster broadcaster, ServicesInterface servicesInterface) {
        this.internal = new TrackerInternals(sdirectory,statusIconInterface, broadcaster, servicesInterface);
        this.broadcaster = broadcaster;
        this.broadcaster.register(onLocation, AppBroadcaster.LOCATION_CHANGED);
    }


    public State getState() {
        return internal.state;
    }



    @Override
    public GpxInformation getLoggerInformation() {
        return internal.logger;
    }


    private final BroadcastReceiver onLocation = new BroadcastReceiver() {

        @Override
        public void onReceive(Object ...data) {
            internal.state.updateTrack();
        }
    };



    public int getPresetIndex() {
        return internal.presetIndex;
    }



    @Override
    public void appendStatusText(StringBuilder builder) {
        builder .append("Log to: ")
                .append(internal.logger.getFile().getPathName());
    }

    public void close() {
        internal.close();
        broadcaster.unregister(onLocation);
    }

}
