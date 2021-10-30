package ch.bailu.aat_lib.service.tracker;

import ch.bailu.aat_lib.dispatcher.AppBroadcaster;

public abstract class State implements  StateInterface {
    protected final TrackerInternals internal;


    public State(TrackerInternals ti) {
        internal = ti;

        internal.logger.setState(getStateID());
    }

    public State() {
        internal = null;
    }

    public void preferencesChanged() {}
}
