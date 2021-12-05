package ch.bailu.aat.activities;

import ch.bailu.aat.app.AndroidAppContext;
import ch.bailu.aat_lib.app.AppContext;

public abstract class ActivityContext extends AbsDispatcher {

    private AppContext appContext = null;
    public AppContext getAppContext() {
        if (appContext == null) {
            appContext = new AndroidAppContext(this, getServiceContext());
        }
        return appContext;
    }
}
