package ch.bailu.aat_lib.service.location;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.presets.SolidMissingTrigger;

public final class MissingTrigger extends LocationStackChainedItem {
    private int triggerMillis=15000;
    private long stamp = System.currentTimeMillis();

    public MissingTrigger(LocationStackItem n) {
        super(n);
    }


    @Override
    public void passLocation(LocationInformation location) {
        AppLog.d(this, "pass location");

        stamp=location.getTimeStamp();
        super.passLocation(location);


    }

    public boolean isMissingUpdates() {
        return System.currentTimeMillis()-stamp > triggerMillis;
    }

    @Override
    public void onPreferencesChanged(StorageInterface storage, String key, int presetIndex) {
        triggerMillis=new SolidMissingTrigger(storage, presetIndex).getTriggerMillis();
    }


}
