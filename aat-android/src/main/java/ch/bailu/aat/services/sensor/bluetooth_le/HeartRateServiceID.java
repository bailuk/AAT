package ch.bailu.aat.services.sensor.bluetooth_le;

import java.util.UUID;

public class HeartRateServiceID extends ID {

    public final static int SERVICE_ID = 0x180d;
    public final static UUID HEART_RATE_SERVICE = toUUID(SERVICE_ID);
    public final static UUID HEART_RATE_MEASUREMENT = toUUID(0x2a37);
    public final static UUID BODY_SENSOR_LOCATION = toUUID(0x2a38);


}
