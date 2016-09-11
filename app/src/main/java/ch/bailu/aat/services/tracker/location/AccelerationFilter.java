package ch.bailu.aat.services.tracker.location;

import android.content.Context;
import ch.bailu.aat.gpx.GpxDeltaHelper;
import ch.bailu.aat.preferences.SolidAccelerationFilter;

public class AccelerationFilter extends LocationStackChainedItem {
    private float maxAcceleration=99f;
    private LocationInformation oldLocation=null;
    
    public AccelerationFilter(LocationStackItem n) {
        super(n);
    }

    @Override
    public void close() {}

    @Override
    public void newLocation(LocationInformation location) {
        if (oldLocation==null || lowAcceleration(oldLocation,location)) {
            oldLocation=location;
            sendLocation(location);
        } 
    }
    
    private boolean lowAcceleration(LocationInformation a, LocationInformation b) {
        float acceleration = Math.abs(GpxDeltaHelper.getAcceleration(a, b));
        return (acceleration < maxAcceleration);
    }

    @Override
    public void preferencesChanged(Context c, int i) {
        maxAcceleration=new SolidAccelerationFilter(c, i).getMaxAcceleration();
    }

	    
}
