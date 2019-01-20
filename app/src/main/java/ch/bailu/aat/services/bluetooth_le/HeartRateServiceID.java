package ch.bailu.aat.services.bluetooth_le;

import java.util.UUID;

public class HeartRateServiceID extends ID {

    public final static int SERVICE_ID = 0x180d;
    public final static UUID HEART_RATE_SERVICE = toUUID(SERVICE_ID);
    public final static UUID HEART_RATE_MESUREMENT = toUUID(0x2a37);
    public final static UUID BODY_SENSOR_LOCATION = toUUID(0x2a38);


    public static final int KEY_INDEX_BPM = 0;
    public static final int KEY_INDEX_BPM_AVERAGE = 1;
    public static final int KEY_INDEX_RR = 2;
    public static final int KEY_INDEX_CONTACT = 3;
    public static final int KEY_INDEX_LOCATION = 3;


    public static final String[] KEYS = {
            "BPM",
            "AverageBPM",
            "RR",
            "ContactStatus",
            "Location"
    };


    public static final String[] BODY_SENSOR_LOCATIONS = {
            "Other",
            "Chest",
            "Wrist",
            "Finger",
            "Hand",
            "Ear Lobe",
            "Foot"
    };
}
