package ch.bailu.aat_lib.service.location;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.presets.SolidAccuracyFilter;

/**
 * Only pass location if accuracy is more precise than min accuracy.
 * Accuracy represents a diameter of a circle. Unit is meter
 */
public final class AccuracyFilter extends LocationStackChainedItem {
    private float minAccuracy=99f;

    public AccuracyFilter(LocationStackItem n) {
        super(n);
    }

    @Override
    public void close() {}

    @Override
    public void passLocation(LocationInformation location) {

        if (location.getAccuracy() < minAccuracy) {
            AppLog.d(this, "pass location");
            super.passLocation(location);
        } else {
            AppLog.d(this, "do not pass location (low accuracy: " + location.getAccuracy() + " < " + minAccuracy +")");
        }
    }

    @Override
    public void onPreferencesChanged(StorageInterface storage, String key, int presetIndex){
        minAccuracy=new SolidAccuracyFilter(storage, presetIndex).getMinAccuracy();
    }
}
