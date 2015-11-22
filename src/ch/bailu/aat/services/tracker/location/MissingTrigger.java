package ch.bailu.aat.services.tracker.location;

import ch.bailu.aat.preferences.SolidMissingTrigger;
import android.content.Context;

public class MissingTrigger extends LocationStackChainedItem {
    private int triggerMillis=15000;
    private long stamp = System.currentTimeMillis();
    
    public MissingTrigger(LocationStackItem n) {
        super(n);
    }

    @Override
    public void cleanUp() {

    }

    @Override
    public void newLocation(LocationInformation location) {
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
