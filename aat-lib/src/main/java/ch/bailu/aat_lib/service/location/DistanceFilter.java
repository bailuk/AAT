package ch.bailu.aat_lib.service.location;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.gpx.GpxDeltaHelper;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.location.SolidDistanceFilter;

public final class DistanceFilter extends LocationStackChainedItem {
    private LocationInformation oldLocation=null;
    private float minDistance=0f;

    public DistanceFilter(LocationStackItem n) {
        super(n);
    }

    @Override
    public void close() {}

    @Override
    public void passLocation(@Nonnull LocationInformation location) {
        if (oldLocation==null || notTooClose(oldLocation,location)) {
            oldLocation=location;
            super.passLocation(location);
        }
    }

    private boolean notTooClose(LocationInformation a, LocationInformation b) {
        if (minDistance > 90) {
            return GpxDeltaHelper.getDistance(a, b) >= (a.getAccuracy()+b.getAccuracy())/2;
        } else {
            return (GpxDeltaHelper.getDistance(a, b) >= minDistance);
        }
    }

    @Override
    public void onPreferencesChanged(StorageInterface storage, String key, int presetIndex) {
        minDistance = new SolidDistanceFilter(storage, presetIndex).getMinDistance();
    }
}
