package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.util.ui.AppLog;


public class AutopauseTrigger extends LocationStackChainedItem {
    private float triggerSpeed=0f;
    private Trigger trigger=new Trigger(10);

    private final Context context;
    public AutopauseTrigger(Context c, LocationStackItem n) {
        super(n);
        context = c;
    }

    @Override
    public void close() {}

    @Override
    public void newLocation(LocationInformation location) {
        AppLog.d(this, "newLocation() -> send");

        float speed = location.getSpeed();
        if (speed < triggerSpeed) {
            trigger.down();
        } else {
            trigger.up();
        }
        trigger.log(context);
        if (isAutopaused()) AppLog.d(this, "isAutopaused");
        sendLocation(location);
    }

    
    public boolean isAutopaused() {
        return  trigger.isLow(); 
    }

    @Override
    public void preferencesChanged(Context c, int i) {
        triggerSpeed = new SolidAutopause(c, i).getTriggerSpeed();
        trigger=new Trigger(new SolidAutopause(c,i).getTriggerLevel(), trigger);
    }
    
	

}
