package ch.bailu.aat.services.bluetooth_le;

import java.util.UUID;

public class CscServiceID extends ID {

    public final static UUID CSC_SERVICE = toUUID(0x1816);
    public final static UUID CSC_MESUREMENT = toUUID(0x2A5B);
    public final static int BIT_SPEED = 0;
    public final static int BIT_CADENCE = 1;
    public final static int BIT_SPEED_AND_CADENCE = 2;

    public final static UUID CSC_FEATURE = toUUID(0x2A5C);
    public final static UUID CSC_SENSOR_LOCATION = toUUID(0x2A5D);
    public final static UUID CSC_CONTROL_POINT = toUUID(0x2A55);


    public final static String[] SENSOR_LOCATION = {
            "Other",
            "Top of shoe",
            "In shoe",
            "Hip",
            "Front Wheel",
            "Left Crank",
            "Right Crank",
            "Left Pedal",
            "Right Pedal",
            "Front Hub",
            "Rear Dropout",
            "Chainstay",
            "Rear Wheel",
            "Rear Hub",
            "Chest",
            "Spider",
            "Chain Ring",
    };


    public static final int KEY_INDEX_SENSOR_LOCATION = 0;
    public static final int KEY_INDEX_SPEED_SENSOR = 1;
    public static final int KEY_INDEX_CADENCE_SENSOR = 2;
    public static final int KEY_INDEX_CRANK_RPM = 3;
    public static final int KEY_INDEX_CRANK_RPM_AVERAGE = 4;
    public static final int KEY_INDEX_WHEEL_CIRCUMFERENCE = 5;


    public final static String[] KEYS = {
            "SensorLocation",
            "SpeedSensor",
            "CadenceSensor",
            "RevolutionsPerMinute",
            "AbverageRevolutionsPerMinute",
            "WheelCircumference"
    };
}
