package ch.bailu.aat.services.sensor.attributes;

import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.attributes.Keys;

public class PowerAttributes  extends CadenceSpeedAttributes {
    public static final Keys KEYS = new Keys();

    static {
        /* copy all from CadenceSpeedAttributes.KEYS which this class
          supports as well (but we need a distinct Keys instance
          because adding "Power" to CadenceSpeedAttributes.KEYS would
          make that class appear to support "Power") */
        for (int i = 0; i < CadenceSpeedAttributes.KEYS.size(); ++i)
            KEYS.add(CadenceSpeedAttributes.KEYS.getKeyIndex(i));
    }

    public static final int KEY_INDEX_POWER = KEYS.add("Power");

    public int power = 0;

    public PowerAttributes(String l, boolean cadence, boolean speed) {
        super(l, cadence, speed);
    }

    @Override
    public String get(int keyIndex) {
        if (keyIndex == KEY_INDEX_POWER)
            return String.valueOf(power);
        else
            return super.get(keyIndex);
    }

    @Override
    public boolean getAsBoolean(int keyIndex) {
        if (keyIndex == KEY_INDEX_CONTACT && power > 0)
            return true;

        return super.getAsBoolean(keyIndex);
    }

    @Override
    public boolean hasKey(int keyIndex) {
        return KEYS.hasKey(keyIndex);
    }

    @Override
    public int size() {
        return KEYS.size();
    }

    @Override
    public String getAt(int i) {
        return get(KEYS.getKeyIndex(i));
    }

    @Override
    public int getKeyAt(int i) {
        return KEYS.getKeyIndex(i);
    }
}
