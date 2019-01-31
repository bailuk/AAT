package ch.bailu.aat.services.sensor.bluetooth_le;

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


}
