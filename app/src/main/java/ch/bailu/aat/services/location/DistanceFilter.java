package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.gpx.GpxDeltaHelper;
import ch.bailu.aat.preferences.SolidDistanceFilter;
import ch.bailu.aat.util.ui.AppLog;

public class DistanceFilter extends LocationStackChainedItem {
    private LocationInformation oldLocation=null;
    private float minDistance=0f;

    
    public DistanceFilter(LocationStackItem n) {
        super(n);
    }

    @Override
    public void close() {}

    @Override
    public void newLocation(LocationInformation location) {
        AppLog.d(this, "newLocation()");
        if (oldLocation==null || notTooClose(oldLocation,location)) {
            oldLocation=location;
            AppLog.d(this, "send");
            sendLocation(location);
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
    public void preferencesChanged(Context c, int i) {
        minDistance = new SolidDistanceFilter(c,i).getMinDistance();
    }
    

}
