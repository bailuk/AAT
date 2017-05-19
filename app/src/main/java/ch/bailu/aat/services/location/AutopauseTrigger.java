package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.util.ui.AppLog;


public class AutopauseTrigger extends LocationStackChainedItem {
    private float triggerSpeed=0f;
    private Trigger trigger=new Trigger(10);

    private final Context logContext;

    public AutopauseTrigger(Context c, LocationStackItem n) {
        super(n);

        logContext = c;
    }

    public AutopauseTrigger(LocationStackItem n) {
        this(null, n);
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

        if (logContext != null) {
            trigger.log(logContext);
        }

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
