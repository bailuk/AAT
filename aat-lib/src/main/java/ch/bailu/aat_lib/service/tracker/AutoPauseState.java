package ch.bailu.aat_lib.service.tracker;

import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.resources.Res;

public final class AutoPauseState extends State {

    public AutoPauseState(TrackerInternals ti) {
        super(ti);

        try {
            internal.logger.logPause();
            internal.statusIcon.showAutoPause();

        } catch (Exception e) {
            internal.emergencyOff(e);
        }

    }

    @Override
    public int getStateID() {
        return StateID.AUTOPAUSED;
    }

    @Override
    public void preferencesChanged() {
        if (!internal.isReadyForAutoPause()) {
            internal.setState(new OnState(internal));
        }
    }

    @Override
    public void updateTrack() {
        if (! internal.isReadyForAutoPause() ) {
            internal.setState(new OnState(internal));
        }
    }



    @Override
    public void onStartPauseResume() {
        onPauseResume();

    }

    @Override
    public void onStartStop() {
        internal.setState(new OffState(internal));

    }

    @Override
    public void onPauseResume() {
        internal.setState(new PauseState(internal));

    }

    @Override
    public String getStartStopText() {
        return Res.str().tracker_stop();
    }

    @Override
    public String getPauseResumeText() {
        return Res.str().tracker_pause();
    }

    @Override
    public String getStartStopIcon() {
        return "playback_stop_inverse";
    }
}
