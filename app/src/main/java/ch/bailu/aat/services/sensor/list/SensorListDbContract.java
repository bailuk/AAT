package ch.bailu.aat.services.sensor.list;

import android.provider.BaseColumns;

public class SensorListDbContract implements BaseColumns {
    public static final String TABLE_NAME = "EnabledSensors";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
}
