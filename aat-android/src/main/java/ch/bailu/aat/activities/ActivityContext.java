package ch.bailu.aat.activities;

import ch.bailu.aat_lib.app.AppContext;

public abstract class ActivityContext extends AbsDispatcher {

    @Override
    public AppContext getAppContext() {
        return null;
    }
}
