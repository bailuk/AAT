package ch.bailu.aat.services.tracker;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;


public class PauseState extends State {

    public PauseState(TrackerInternals ti) {
        super(ti);
        
        try {
            internal.logger.logPause();
            internal.statusIcon.showPause();
            internal.backlight.setToOff();
        } catch (IOException e) {
            internal.emergencyOff(e);
        }
        
    }

    @Override
    public int getStateID() {
        return GpxInformation.ID.STATE_PAUSE;
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
    public int getStatusTextID() {
        return R.string.status_paused;
    }

    @Override
    public int getStartStopTextID() {
        return R.string.tracker_stop;
    }

    @Override
    public int getPauseResumeTextID() {
        return R.string.tracker_resume;
    }

    @Override
    public int getStartPauseResumeTextID() {
        return R.string.tracker_resume;
    }

    @Override
    public int getStartStopIconID() {
        return R.drawable.media_playback_stop_inverse;
    }


}