package ch.bailu.aat_lib.gpx.attributes;

public class CadenceSpeedAttributes   extends GpxAttributes {


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

    public static final Keys KEYS = new Keys();


    public static final int KEY_INDEX_SENSOR_LOCATION = KEYS.add("Location");
    public static final int KEY_INDEX_SPEED_SENSOR = KEYS.add("SpeedSensor");
    public static final int KEY_INDEX_CADENCE_SENSOR = KEYS.add("CadenceSensor");
    public static final int KEY_INDEX_CRANK_RPM = KEYS.add("Cadence");
    public static final int KEY_INDEX_WHEEL_CIRCUMFERENCE = KEYS.add("WheelCircumference");
    public static final int KEY_INDEX_CONTACT = KEYS.add("Contact");
    public static final int KEY_INDEX_CIRCUMFERENCE_DEBUG = KEYS.add("Debug");


    public int cadence_rpm = 0;
    public int cadence_rpm_average = 0;
    public float circumferenceSI = 0f;

    public String circumferenceDebugString = "--";

    public final String location;
    public final boolean isCadenceSensor, isSpeedSensor;


    public CadenceSpeedAttributes(String l, boolean cadence, boolean speed) {
        location = l;
        isCadenceSensor = cadence;
        isSpeedSensor = speed;
    }



    @Override
    public String get(int keyIndex) {
        if (keyIndex == KEY_INDEX_SENSOR_LOCATION) {
            return location;

        } else if (keyIndex == KEY_INDEX_CADENCE_SENSOR) {
            return String.valueOf(isCadenceSensor);

        } else if (keyIndex == KEY_INDEX_SPEED_SENSOR) {
            return String.valueOf(isSpeedSensor);

        } else if (keyIndex == KEY_INDEX_CRANK_RPM) {
            return String.valueOf(cadence_rpm_average);

        } else if (keyIndex == KEY_INDEX_WHEEL_CIRCUMFERENCE) {
            return String.valueOf(circumferenceSI);

        } else if (keyIndex == KEY_INDEX_CONTACT) {
            return String.valueOf(getAsBoolean(keyIndex));

        } else if (keyIndex == KEY_INDEX_CIRCUMFERENCE_DEBUG) {
            return circumferenceDebugString;
        }


        return NULL_VALUE;
    }


    @Override
    public boolean getAsBoolean(int keyIndex) {
        if (keyIndex == KEY_INDEX_CONTACT) {
            boolean cadenceContact = isCadenceSensor && cadence_rpm > 0;
            boolean speedContect = isSpeedSensor && circumferenceSI > 0f;

            return cadenceContact || speedContect;


        } else {
            return super.getAsBoolean(keyIndex);
        }
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
