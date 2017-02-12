package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.preferences.SolidMissingTrigger;
import ch.bailu.aat.util.ui.AppLog;

public class MissingTrigger extends LocationStackChainedItem {
    private int triggerMillis=15000;
    private long stamp = System.currentTimeMillis();
    
    public MissingTrigger(LocationStackItem n) {
        super(n);
    }

    
    @Override
    public void newLocation(LocationInformation location) {
//        AppLog.d(this, "newLocation() -> send");
//        if (isMissingUpdates()) AppLog.d(this, "isMissingUpdates");
        stamp=location.getTimeStamp();
        sendLocation(location);


    }

    public boolean isMissingUpdates() {
        return System.currentTimeMillis()-stamp > triggerMillis;
    }

    @Override
    public void preferencesChanged(Context c, int i) {
        triggerMillis=new SolidMissingTrigger(c,i).getTriggerMillis();
    }
    

}
