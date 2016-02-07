package ch.bailu.aat.services.tracker.location;

import ch.bailu.aat.preferences.SolidAutopause;
import android.content.Context;


public class AutopauseTrigger extends LocationStackChainedItem {
    private float triggerSpeed=0f;
    private Trigger trigger=new Trigger(10);
    
    public AutopauseTrigger(LocationStackItem n) {
        super(n);
    }

    @Override
    public void close() {}

    @Override
    public void newLocation(LocationInformation location) {
        float speed = location.getSpeed();
        if (speed < triggerSpeed) {
            trigger.down();
        } else {
            trigger.up();
        }
     
        sendLocation(location);
    }

    
    public boolean isAutopaused() {
        return  trigger.isLow(); 
    }

    @Override
    public void preferencesChanged(Context c, int i) {
        triggerSpeed = new SolidAutopause(c, i).getTriggerSpeed();
        trigger=new Trigger(new SolidAutopause(c,i).getTriggerLevel());
    }
    
	

}
