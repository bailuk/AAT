package ch.bailu.aat_lib.service.tracker;

import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.resources.Res;

public final class OffState extends State {

    public OffState(TrackerInternals ti) {
        super(ti);

        internal.logger.close();
        internal.statusIcon.hide();
        internal.unlockService();
        internal.rereadPreferences();
    }

    @Override
    public void updateTrack() {}

    @Override
    public int getStateID() {
        return StateID.OFF;
    }

    @Override
    public void onStartPauseResume() {
        onStartStop();

    }

    @Override
    public void onStartStop() {
        try {
            internal.logger = internal.createLogger();

            internal.lockService();

            internal.state = new OnState(internal);

        } catch (Exception e) {
            AppLog.e(this, e);
            internal.logger = Logger.createNullLogger();
        }
    }

    @Override
    public void onPauseResume() {
    }


    @Override
    public String getStartStopText() {
        return Res.str().tracker_start();
    }

    @Override
    public String getPauseResumeText() {
        return Res.str().tracker_pause();
    }

    @Override
    public int getStartStopIconID() {
        return Res.getIconResource("R.drawable.media_playback_start_inverse");
    }


    @Override
    public void preferencesChanged() {
        internal.rereadPreferences();
    }
}
