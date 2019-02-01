package ch.bailu.aat.services.sensor.attributes;

public class CadenceSpeedAttributes   extends IndexedAttributes {


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
    public static final int KEY_INDEX_WHEEL_CIRCUMFERENCE = 4;
    public static final int KEY_INDEX_CONTACT = 5;


    public final static String[] KEYS = {
            "Location",
            "SpeedSensor",
            "CadenceSensor",
            "Cadence",
            "WheelCircumference",
            "Contact"
    };


    public int cadence_rpm = 0;
    public int cadence_rpm_average = 0;
    public float circumferenceSI = 0f;

    public final String location;
    public final boolean isCadenceSensor, isSpeedSensor;


    public CadenceSpeedAttributes(String l, boolean cadence, boolean speed) {
        super(KEYS);
        location = l;
        isCadenceSensor = cadence;
        isSpeedSensor = speed;
    }



    @Override
    public String getValue(int index) {
        if (index == KEY_INDEX_SENSOR_LOCATION) {
            return location;

        } else if (index == KEY_INDEX_CADENCE_SENSOR) {
            return String.valueOf(isCadenceSensor);

        } else if (index == KEY_INDEX_SPEED_SENSOR) {
            return String.valueOf(isSpeedSensor);

        } else if (index == KEY_INDEX_CRANK_RPM) {
            return String.valueOf(cadence_rpm_average);

        } else if (index == KEY_INDEX_WHEEL_CIRCUMFERENCE) {
            return String.valueOf(circumferenceSI);

        } else if (index == KEY_INDEX_CONTACT) {
            if (cadence_rpm == 0) return "...";
            return "";
        }


        return NULL_VALUE;
    }
}
