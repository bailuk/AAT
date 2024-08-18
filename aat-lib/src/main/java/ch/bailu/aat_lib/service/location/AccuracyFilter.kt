package ch.bailu.aat_lib.service.location;

import javax.annotation.Nonnull;

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
    public void passLocation(@Nonnull LocationInformation location) {
        if (location.getAccuracy() < minAccuracy) {
            super.passLocation(location);
        }
    }

    @Override
    public void onPreferencesChanged(StorageInterface storage, String key, int presetIndex){
        minAccuracy=new SolidAccuracyFilter(storage, presetIndex).getMinAccuracy();
    }
}
