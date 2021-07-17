package ch.bailu.aat_lib.service.location;

import ch.bailu.aat_lib.gpx.GpxDeltaHelper;
import ch.bailu.aat_lib.logger.AppLog;
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
    public void passLocation(LocationInformation location) {
        if (oldLocation==null || notTooClose(oldLocation,location)) {
            AppLog.d(this, "pass location");
            oldLocation=location;
            super.passLocation(location);
        } else {
            if (oldLocation == null) {
                AppLog.d(this, "do not pass (no old location)");
            } else {
                AppLog.d(this, "do not pass (too close)");
            }
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
