package ch.bailu.aat.services.tracker;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;

public abstract class State implements GpxInformation.ID {
    protected final TrackerInternals internal;
    
    
    public State(TrackerInternals ti) {
        internal = ti;
        
        internal.logger.setState(getStateID());
        AppBroadcaster.broadcast(internal.serviceContext, AppBroadcaster.TRACKER);
        AppBroadcaster.broadcast(internal.serviceContext, AppBroadcaster.TRACKER_STATE);

    }
    
    public State() {
        internal = null;
    }
    
    
    public void onTimer() {}
    public void preferencesChanged() {}

    
    public abstract void onStartPauseResume();
    public abstract void onStartStop();
    public abstract void onPauseResume();
    
    
    public abstract int getStateID();

    
    public abstract int getStatusTextID();
    public abstract int getStartStopTextID();
    public abstract int getPauseResumeTextID();
    public abstract int getStartPauseResumeTextID();
    
    public abstract int getStartStopIconID();
}
