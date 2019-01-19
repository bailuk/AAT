package ch.bailu.aat.services.bluetooth_le;

import java.util.UUID;

public class HeartRateServiceID {

    public final static int SERVICE_ID = 0x180d;
    public final static UUID HEART_RATE_SERVICE = ID.toUUID(SERVICE_ID);
    public final static UUID HEART_RATE_MESUREMENT = ID.toUUID(0x2a37);
    public final static UUID BODY_SENSOR_LOCATION = ID.toUUID(0x2a38);


    public static final int BPM_KEY_INDEX=0;
    public static final int BPMA_KEY_INDEX=1;
    public static final int RR_KEY_INDEX=2;
    public static final int CONTACT_KEY_INDEX=3;

    public static final String[] KEYS = {
            "BPM",
            "AverageBPM",
            "RR",
            "ContactStatus",
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
