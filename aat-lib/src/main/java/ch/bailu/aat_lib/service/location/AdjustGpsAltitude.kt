package ch.bailu.aat_lib.service.location;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitude;
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitudeValue;
import ch.bailu.aat_lib.preferences.location.SolidProvideAltitude;

public final class AdjustGpsAltitude extends LocationStackChainedItem {

    private final SolidProvideAltitude saltitude;

    private final SolidAdjustGpsAltitude senabled;
    private final SolidAdjustGpsAltitudeValue sadjust;

    private int adjust;
    private boolean enabled;

    private final AltitudeCache altitude = new AltitudeCache();

    public AdjustGpsAltitude(LocationStackItem n, StorageInterface s) {
        super(n);

        senabled  = new SolidAdjustGpsAltitude(s);
        sadjust = new SolidAdjustGpsAltitudeValue(s);

        saltitude = new SolidProvideAltitude(s, SolidUnit.SI);

        adjust    = sadjust.getValue();
        enabled   = senabled.isEnabled();
    }

    @Override
    public void passLocation(@Nonnull LocationInformation location) {
        if (altitude.set(location) && enabled) {
            location.setAltitude(location.getAltitude() + adjust);
        }
        super.passLocation(location);
    }

    @Override
    public void onPreferencesChanged(StorageInterface storage, String key, int presetIndex) {
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
