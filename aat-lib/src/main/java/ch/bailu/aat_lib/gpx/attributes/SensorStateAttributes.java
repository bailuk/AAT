package ch.bailu.aat_lib.gpx.attributes;

import ch.bailu.aat_lib.service.sensor.SensorState;

public class SensorStateAttributes extends GpxAttributes {

    private static final Keys KEYS = new Keys();
    public static final int KEY_SENSOR_COUNT = KEYS.add("count");
    public static final int KEY_SENSOR_OVERVIEW = KEYS.add("overview");


    private final int sensors;


    public SensorStateAttributes(int s) {
        sensors = s;
    }


    @Override
    public String get(int key) {
        if (key == KEY_SENSOR_COUNT) {
            return String.valueOf(sensors);
        } else if (key == KEY_SENSOR_OVERVIEW) {
            return SensorState.getOverviewString();
        }
        return NULL_VALUE;
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
        return get(getKeyAt(i));
    }

    @Override
    public int getKeyAt(int i) {
        return KEYS.getKeyIndex(i);
    }
}
