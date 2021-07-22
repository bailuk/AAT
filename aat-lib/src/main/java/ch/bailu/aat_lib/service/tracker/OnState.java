package ch.bailu.aat_lib.service.tracker;

import java.io.IOException;

import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.location.LocationServiceInterface;

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
            final LocationServiceInterface l = internal.services.getLocationService();

            try {
                GpxInformation newLocation = l.getLoggableLocationOrNull(location);

                if (newLocation != null) {
                    location = newLocation;
                    final GpxAttributes attr = attributes.collect(internal.services.getSensorService());
                    internal.logger.log(location, attr);
                }
            } catch (IOException e) {
                internal.emergencyOff(e);
            }
            internal.broadcaster.broadcast(AppBroadcaster.TRACKER);
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
    public String getStartStopText() {
        return Res.str().tracker_stop();
    }

    @Override
    public String getPauseResumeText() {
        return Res.str().tracker_pause();  }

    @Override
    public int getStartStopIconID() {
        return Res.getIconResource("R.drawable.media_playback_stop_inverse");
    }

}
