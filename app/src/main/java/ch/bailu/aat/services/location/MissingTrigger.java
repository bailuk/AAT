package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.preferences.presets.SolidMissingTrigger;

public class MissingTrigger extends LocationStackChainedItem {
    private int triggerMillis=15000;
    private long stamp = System.currentTimeMillis();
    
    public MissingTrigger(LocationStackItem n) {
        super(n);
    }

    
    @Override
    public void passLocation(LocationInformation location) {
        stamp=location.getTimeStamp();
        super.passLocation(location);


    }

    public boolean isMissingUpdates() {
        return System.currentTimeMillis()-stamp > triggerMillis;
    }

    @Override
    public void preferencesChanged(Context c, String key, int presetIndex) {
        triggerMillis=new SolidMissingTrigger(c, presetIndex).getTriggerMillis();
    }
    

}
