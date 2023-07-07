package ch.bailu.aat_lib.dispatcher;


import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.util.Timer;

public class TrackerTimerSource extends ContentSource {
    private static final int INTERVAL=500;

    private final ServicesInterface scontext;
    private final Timer timer;

    public TrackerTimerSource(ServicesInterface sc, Timer timer) {
        this.timer = timer;
        scontext = sc;
    }

    @Override
    public void requestUpdate() {
        sendUpdate(InfoID.TRACKER_TIMER,
                scontext.getTrackerService().getInfo());
        timer.kick(INTERVAL, this::requestUpdate);
    }

    @Override
    public void onPause() {
        timer.cancel();
    }

    @Override
    public void onResume() {
        timer.kick(INTERVAL, this::requestUpdate);
    }

    @Override
    public int getIID() {
        return InfoID.TRACKER_TIMER;
    }

    @Override
    public GpxInformation getInfo() {
        return scontext.getTrackerService().getInfo();
    }
}
