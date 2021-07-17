package ch.bailu.aat_lib.service.location;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.presets.SolidTrackerAutopause;

public final class AutopauseTrigger extends LocationStackChainedItem {
    private float triggerSpeed=0f;
    private Trigger trigger=new Trigger(10);


    public AutopauseTrigger(LocationStackItem n) {
        super(n);
    }


    @Override
    public void close() {}

    @Override
    public void passLocation(LocationInformation location) {


        float speed = location.getSpeed();
        if (speed < triggerSpeed) {
            trigger.down();
        } else {
            trigger.up();
        }

        trigger.log();

        AppLog.d(this, "pass location");
        super.passLocation(location);
    }


    public boolean isAutopaused() {
        return  trigger.isLow();
    }

    @Override
    public void onPreferencesChanged(StorageInterface storage, String key, int presetIndex) {
        triggerSpeed = new SolidTrackerAutopause(storage, presetIndex).getTriggerSpeed();
        trigger = new Trigger(new SolidTrackerAutopause(storage, presetIndex).getTriggerLevel(), trigger);
    }



}
