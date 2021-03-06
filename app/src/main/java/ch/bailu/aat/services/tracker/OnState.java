package ch.bailu.aat.services.tracker;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.services.location.LocationService;

public final class OnState extends State {

    private final AttributesCollector attributes = new AttributesCollector();

    private GpxInformation location = GpxInformation.NULL;

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

            if (l.hasLoggableLocation(location)) {
                try {
                    location = l.getLoggableLocation();
                    internal.logger.log(location, attr);
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
