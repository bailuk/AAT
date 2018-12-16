package ch.bailu.aat.dispatcher;


import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.Timer;

public class TrackerTimerSource extends ContentSource {
    private static final int INTERVAL=500;

    private final ServiceContext scontext;
    private final Timer timer;

    public TrackerTimerSource(ServiceContext sc) {
        timer = new Timer(() -> requestUpdate(), INTERVAL);

        scontext = sc;
    }



    @Override
    public void requestUpdate() {
        sendUpdate(InfoID.TRACKER_TIMER,
                scontext.getTrackerService().getLoggerInformation());
        timer.kick();
    }


    @Override
    public void onPause() {
        timer.cancel();

    }


    @Override
    public void onResume() {
        timer.kick();
    }

    @Override
    public int getIID() {
        return InfoID.TRACKER_TIMER;
    }

    @Override
    public GpxInformation getInfo() {
        return scontext.getTrackerService().getLoggerInformation();
    }

}
