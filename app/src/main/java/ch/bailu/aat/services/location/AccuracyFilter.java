package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.preferences.SolidAccuracyFilter;


public class AccuracyFilter extends LocationStackChainedItem {
    private float minAccuracy=99f;

    public AccuracyFilter(LocationStackItem n) {
        super(n);
    }

    @Override
    public void close() {}

    @Override
    public void newLocation(LocationInformation location) {
        if (location.getAccuracy() < minAccuracy) sendLocation(location);
    }

    @Override
    public void preferencesChanged(Context c, int i) {
        minAccuracy=new SolidAccuracyFilter(c, i).getMinAccuracy();
    }
    

}
