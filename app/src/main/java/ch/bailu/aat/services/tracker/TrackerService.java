package ch.bailu.aat.services.tracker;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public class TrackerService extends VirtualService {

    private final TrackerInternals internal;

    public TrackerService(ServiceContext sc) {
        super(sc);
        internal = new TrackerInternals(getSContext());
    }


    public State getState() {
        return internal.state;
    }


    public GpxInformation getTrackerInformation() {
        return internal.logger;
    }


    public GpxInformation getLocation() {
        return internal.location.getLocationInformation();
    }

    @Override
    public void appendStatusText(StringBuilder builder) {




        builder.append("<p>Log to: ");
        builder.append(internal.logger.getPath());
        builder.append("</p>");

        internal.location.appendStatusText(builder);
    }

    @Override
    public void close() {
        internal.close();
    }

}
