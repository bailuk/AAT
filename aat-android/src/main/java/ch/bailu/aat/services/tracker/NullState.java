package ch.bailu.aat.services.tracker;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.StateID;

public final class NullState extends State {

    @Override
    public int getStateID() {
        return StateID.OFF;
    }



    @Override
    public void updateTrack() {}

    @Override
    public void onStartPauseResume() {}

    @Override
    public void onStartStop() {}

    @Override
    public void onPauseResume() {}


    @Override
    public int getStartStopTextID() {
        return R.string.tracker_start;
    }

    @Override
    public int getPauseResumeTextID() {
        return R.string.tracker_pause;
    }

    @Override
    public int getStartStopIconID() {
        return R.drawable.media_playback_start_inverse;
    }
}
