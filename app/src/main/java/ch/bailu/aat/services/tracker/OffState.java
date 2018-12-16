package ch.bailu.aat.services.tracker;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.util.ui.AppLog;


public class OffState extends State {

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
            AppLog.e(internal.scontext.getContext(), e);
            internal.logger = Logger.createNullLogger();
        }
    }

    @Override
    public void onPauseResume() {
    }    
    
    
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
        return R.drawable.media_playback_start_inverse;
    }


    @Override
    public void preferencesChanged() {
        internal.rereadPreferences();
    }
}
