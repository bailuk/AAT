package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.preferences.SolidAccuracyFilter;
import ch.bailu.aat.util.ui.AppLog;


public class AccuracyFilter extends LocationStackChainedItem {
    private float minAccuracy=99f;

    public AccuracyFilter(LocationStackItem n) {
        super(n);
    }

    @Override
    public void close() {}

    @Override
    public void newLocation(LocationInformation location) {
        AppLog.d(this, "newLocation()");

        if (location.getAccuracy() < minAccuracy) {
            AppLog.d(this, "send");
            sendLocation(location);
        }
    }

    @Override
    public void preferencesChanged(Context c, int i) {
        minAccuracy=new SolidAccuracyFilter(c, i).getMinAccuracy();
    }
    

}
