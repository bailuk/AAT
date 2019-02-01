package ch.bailu.aat.services.tracker;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxAttributesStatic;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.services.sensor.SensorService;
import ch.bailu.aat.services.sensor.attributes.CadenceSpeedAttributes;
import ch.bailu.aat.services.sensor.attributes.HeartRateAttributes;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.services.location.LocationService;

public class OnState extends State {

    private final AttributesCollector attributes = new AttributesCollector();


    public OnState(TrackerInternals tracker) {
        super(tracker);


        if (tracker.isReadyForAutoPause()) {
            tracker.state = new AutoPauseState(tracker);
            
        } else {
            tracker.statusIcon.showOn();
        }

    }

    @Override
    public int getStateID() {
        return StateID.ON;
    }


    @Override
    public void preferencesChanged() {

    }


    
    @Override
    public void updateTrack() {
        if (internal.isReadyForAutoPause()) {
            internal.state = new AutoPauseState(internal);

        } else  {
            final LocationService l = internal.scontext.getLocationService();
            final GpxAttributes attr = attributes.collect(internal.scontext);

            if (l.hasLoggableLocation()) {
                try {
                    internal.logger.log(l.getCleanLocation(), attr);
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