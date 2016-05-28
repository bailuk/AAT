package ch.bailu.aat.services.tracker;

import ch.bailu.aat.R;

public class NullState extends State {

    @Override
    public int getStateID() {
        return STATE_OFF;
    }



    @Override
    public void onStartPauseResume() {}

    @Override
    public void onStartStop() {}

    @Override
    public void onPauseResume() {}
    
    
    @Override
    public int getStatusTextID() {
        return R.string.off;
    }

    @Override
    public int getStartStopTextID() {
        return R.string.tracker_start;
    }

    @Override
    public int getPauseResumeTextID() {
        return R.string.tracker_pause;
    }

    @Override
    public int getStartPauseResumeTextID() {
        return R.string.tracker_start;
    }

    @Override
    public int getStartStopIconID() {
        return R.drawable.media_playback_start;
    }
}
