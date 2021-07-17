package ch.bailu.aat_lib.service.tracker;

import ch.bailu.aat_lib.dispatcher.AppBroadcaster;

public abstract class State  {
    protected final TrackerInternals internal;


    public State(TrackerInternals ti) {
        internal = ti;

        internal.logger.setState(getStateID());
        internal.broadcaster.broadcast(AppBroadcaster.TRACKER);
    }

    public State() {
        internal = null;
    }


    public abstract void updateTrack();
    public void preferencesChanged() {}


    public abstract void onStartPauseResume();
    public abstract void onStartStop();
    public abstract void onPauseResume();


    public abstract int getStateID();

    public abstract String getStartStopText();
    public abstract String getPauseResumeText();

    public abstract int getStartStopIconID();
}
