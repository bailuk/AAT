package ch.bailu.aat.services.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.Timer;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public class TrackerService extends VirtualService {

    private final TrackerInternals internal;


    public TrackerService(ServiceContext sc) {
        super(sc);
        internal = new TrackerInternals(getSContext());

        AppBroadcaster.register(getContext(), onLocation, AppBroadcaster.LOCATION_CHANGED);
    }


    public State getState() {
        return internal.state;
    }


    /**
     *
     * @return Unfiltered track infromation. Exactly the way it is logged.
     */
    public GpxInformation getTrackerInformation() {
        return internal.logger;
    }



    private final BroadcastReceiver onLocation = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            internal.state.updateTrack();
        }
    };



    @Override
    public void appendStatusText(StringBuilder builder) {
        builder.append("<p>Log to: ");
        builder.append(internal.logger.getPath());
        builder.append("</p>");
    }

    @Override
    public void close() {
        internal.close();
        getContext().unregisterReceiver(onLocation);
    }

}
