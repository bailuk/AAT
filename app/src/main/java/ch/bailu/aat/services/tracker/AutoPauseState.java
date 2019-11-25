package ch.bailu.aat.services.tracker;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.StateID;


public final class AutoPauseState extends State {


    public AutoPauseState(TrackerInternals ti) {
        super(ti);

        try {
            internal.logger.logPause();
            internal.statusIcon.showAutoPause();

        } catch (IOException e) {
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
            internal.state = new OnState(internal);
        }
    }

    @Override
    public void updateTrack() {
        if (! internal.isReadyForAutoPause() ) {
            internal.state = new OnState(internal);
        }
    }



    @Override
    public void onStartPauseResume() {
        onPauseResume();

    }

    @Override
    public void onStartStop() {
        internal.state = new OffState(internal);

    }

    @Override
    public void onPauseResume() {
        internal.state = new PauseState(internal);

    }

    @Override
    public int getStartStopTextID() {
        return R.string.tracker_stop;
    }

    @Override
    public int getPauseResumeTextID() {
        return R.string.tracker_pause;
    }

    @Override
    public int getStartStopIconID() {
        return R.drawable.media_playback_stop_inverse;
    }
}

