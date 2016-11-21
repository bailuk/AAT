package ch.bailu.aat.services.tracker;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.location.LocationService;

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
        return StateID.ON;
    }

    @Override
    public void preferencesChanged() {
        internal.backlight.setToPreferred(internal.sbacklight);
    }


    
    @Override
    public void updateTrack() {
        if (internal.isReadyForAutoPause()) {
            internal.state = new AutoPauseState(internal);

        } else  {
            final LocationService l = internal.scontext.getLocationService();

            if (l.hasLoggableLocation()) {
                try {
                    internal.logger.log(l.getCleanLocation());
                } catch (IOException e) {
                    internal.emergencyOff(e);
                }
            } 
            AppBroadcaster.broadcast(internal.scontext.getContext(), AppBroadcaster.TRACKER);
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
        return R.drawable.media_playback_stop_inverse;
    }

}