package ch.bailu.aat.services.tracker;

import java.io.IOException;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.R;

public class OnState extends State {

    public OnState(TrackerInternals tracker) {
        super(tracker);
        
        if (tracker.isReadyForAutoPause()) {
            tracker.state = new AutoPauseState(tracker);
            
        } else {
            tracker.statusIcon.showOn();
            internal.backlight.setToPreferred(internal.sbacklight);
        }

    }

    @Override
    public int getStateID() {
        return STATE_ON;
    }

    @Override
    public void preferencesChanged() {
        internal.backlight.setToPreferred(internal.sbacklight);
    }


    
    @Override
    public void onTimer() {
        if (internal.isReadyForAutoPause()) {
            internal.state = new AutoPauseState(internal);

        } else  {
            if (internal.location.hasLoggableLocation()) {
                try {
                    internal.logger.log(internal.location.getCleanLocation());
                } catch (IOException e) {
                    internal.emergencyOff(e);
                }
            } 
            AppBroadcaster.broadcast(internal.serviceContext, AppBroadcaster.TRACKER);
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
    public int getStatusTextID() {
        return R.string.on;
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
    public int getStartPauseResumeTextID() {
        return R.string.tracker_pause;
    }

    @Override
    public int getStartStopIconID() {
        return R.drawable.media_playback_stop;
    }

}