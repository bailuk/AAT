package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.preferences.general.SolidUnit;
import ch.bailu.aat.preferences.location.SolidAdjustGpsAltitude;
import ch.bailu.aat.preferences.location.SolidAdjustGpsAltitudeValue;
import ch.bailu.aat.preferences.location.SolidProvideAltitude;

public class AdjustGpsAltitude extends LocationStackChainedItem {

    private final SolidProvideAltitude saltitude;

    private final SolidAdjustGpsAltitude senabled;
    private final SolidAdjustGpsAltitudeValue sadjust;

    private int adjust;
    private boolean enabled;


    private final AltitudeCache altitude = new AltitudeCache();


    public AdjustGpsAltitude(LocationStackItem n, Context c) {
        super(n);

        senabled  = new SolidAdjustGpsAltitude(c);
        sadjust = new SolidAdjustGpsAltitudeValue(c);

        saltitude = new SolidProvideAltitude(c, SolidUnit.SI);

        adjust    = sadjust.getValue();
        enabled   = senabled.isEnabled();
    }



    @Override
    public void passLocation(LocationInformation l) {
        if (altitude.set(l) && enabled) {
            l.setAltitude(l.getAltitude() + adjust);
        }
        super.passLocation(l);
    }


    @Override
    public void preferencesChanged(Context c, String key, int presetIndex) {
        if (senabled.hasKey(key)) {
            enabled = senabled.getValue();

        } else if (sadjust.hasKey(key)) {
            adjust = sadjust.getValue();

        } else if (saltitude.hasKey(key)) {
            altitude.setGPSAdjustValue(sadjust, saltitude.getValue());
        }
    }


    private static class AltitudeCache {
        private final static long MAX_AGE = 10 * 1000;

        private int altitude = 0;
        private long time = 0;

        public boolean set(LocationInformation l) {
            if (l.hasAltitude()) {
                altitude = (int) l.getAltitude();
                time = l.getTimeStamp();
                return true;
            }
            return false;
        }


        public void setGPSAdjustValue(SolidAdjustGpsAltitudeValue sadjust, int currentAltitude) {
            long age = System.currentTimeMillis() - time;

            if (age < MAX_AGE) {
                sadjust.setValue(currentAltitude - altitude);
            }
        }
    }
}
