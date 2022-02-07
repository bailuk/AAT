package ch.bailu.aat.dispatcher;


import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.dispatcher.ContentSource;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.util.Timer;

public class TrackerTimerSource extends ContentSource {
    private static final int INTERVAL=500;

    private final ServiceContext scontext;
    private final Timer timer;

    public TrackerTimerSource(ServiceContext sc, Timer timer) {
        this.timer = timer;

        scontext = sc;
    }



    @Override
    public void requestUpdate() {
        sendUpdate(InfoID.TRACKER_TIMER,
                scontext.getTrackerService().getLoggerInformation());
        timer.kick(() -> requestUpdate(), INTERVAL);
    }


    @Override
    public void onPause() {
        timer.cancel();

    }


    @Override
    public void onResume() {
        timer.kick(()->requestUpdate(), INTERVAL);
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
