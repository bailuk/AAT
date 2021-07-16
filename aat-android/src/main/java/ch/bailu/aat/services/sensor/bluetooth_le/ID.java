package ch.bailu.aat.services.sensor.bluetooth_le;

import java.util.UUID;

public class ID {

    public final static int MINUTE = 60 * 1024;


    public static Boolean isBitSet(byte b, int bit) {
        return (b & (1 << bit)) != 0;
    }

    public static String toIDString(UUID uuid) {
        return Integer.toHexString(toID(uuid));
    }
    public static String toIDString(int id) {
        return Integer.toHexString(id);
    }

    public static int toID(UUID uuid) {
        return (int) (uuid.getMostSignificantBits() >> 32);
    }

    public static UUID toUUID(int id) {
        return UUID.fromString("0000" + toIDString(id) + "-0000-1000-8000-00805f9b34fb");
    }
}
