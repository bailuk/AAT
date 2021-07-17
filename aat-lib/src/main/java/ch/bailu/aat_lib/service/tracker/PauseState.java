package ch.bailu.aat_lib.service.tracker;

import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.resources.Res;

public final class PauseState extends State {



    public PauseState(TrackerInternals ti) {
        super(ti);


        try {
            internal.logger.logPause();
            internal.statusIcon.showPause();
        } catch (Exception e) {
            internal.emergencyOff(e);
        }

    }

    @Override
    public void updateTrack() {}

    @Override
    public int getStateID() {
        return StateID.PAUSE;
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
        internal.state = new OnState(internal);

    }

    @Override
    public String getStartStopText() {
        return Res.str().tracker_stop();
    }

    @Override
    public String getPauseResumeText() {
        return Res.str().tracker_resume();
    }

    @Override
    public int getStartStopIconID() {
        return Res.getIconResource("R.drawable.media_playback_stop_inverse");
    }


}
