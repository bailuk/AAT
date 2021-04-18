package ch.bailu.aat.services.sensor.bluetooth_le;

import java.util.UUID;

/**
 * Definitions for the Cycling Power GATT service.
 *
 * @see https://www.bluetooth.com/specifications/specs/cycling-power-service-1-1/
 * @see https://www.bluetooth.com/specifications/specs/cycling-power-profile-1-1/
 * @see https://www.bluetooth.com/specifications/assigned-numbers/
 */
public class CyclingPowerID extends ID {

    public final static UUID CYCLING_POWER_SERVICE = toUUID(0x1818);

    public final static UUID SENSOR_LOCATION = toUUID(0x2A5D);
    public final static UUID CYCLING_POWER_MEASUREMENT = toUUID(0x2a63);
    public final static UUID CYCLING_POWER_VECTOR = toUUID(0x2a64);
    public final static UUID CYCLING_POWER_FEATURE = toUUID(0x2a65);
    public final static UUID CYCLING_POWER_CONTROL_POINT = toUUID(0x2a66);

    public final static int FEATURE_BIT_WHEEL_REVOLUTIONS = 2;
    public final static int FEATURE_BIT_CRANK_REVOLUTIONS = 3;

    public final static int BIT_PEDAL_POWER_BALANCE = 0;
    public final static int BIT_PEDAL_POWER_BALANCE_REFERENCE = 1;
    public final static int BIT_ACCUMULATED_TORQUE = 2;
    public final static int BIT_ACCUMULATED_TORQUE_SOURCE = 3;
    public final static int BIT_WHEEL_REVOLUTION_DATA = 4;
    public final static int BIT_CRANK_REVOLUTION_DATA = 5;
    public final static int BIT_EXTREME_FORCE_MAGNITUDES = 6;
    public final static int BIT_EXTREME_TORQUE_MAGNITUDES = 7;
    public final static int BIT_EXTREME_ANGLES = 8;
    public final static int BIT_TOP_DEAD_SPOT_ANGLE = 9;
    public final static int BIT_BOTTOM_DEAD_SPOT_ANGLE = 10;
    public final static int BIT_ACCUMULATED_ENERGY = 11;
    public final static int BIT_OFFSET_COMPENSATION_INDICATOR = 12;
}
