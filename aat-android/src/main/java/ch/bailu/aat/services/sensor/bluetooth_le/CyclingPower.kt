
package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;

import androidx.annotation.NonNull;

import ch.bailu.aat.R;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.Connector;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.attributes.PowerAttributes;

public final class CyclingPower extends CyclingPowerID implements ServiceInterface {
    private String location = PowerAttributes.SENSOR_LOCATION[0];

    private boolean isSpeedSensor = false;
    private boolean isCadenceSensor = false;

    private final Revolution cadence = new Revolution();
    private final Revolution speed = new Revolution();
    private final WheelCircumference wheelCircumference;

    private GpxInformation information = GpxInformation.NULL;

    private boolean valid = false;

    private final Connector connectorPower, connectorSpeed, connectorCadence;
    private final Broadcaster broadcasterPower, broadcasterSpeed, broadcasterCadence;

    private final String name_power, name_speed, name_cadence;

    public CyclingPower(ServiceContext c) {
        wheelCircumference = new WheelCircumference(c, speed);
        connectorPower = new Connector(c.getContext(), InfoID.POWER_SENSOR);
        connectorCadence = new Connector(c.getContext(), InfoID.CADENCE_SENSOR);
        connectorSpeed = new Connector(c.getContext(), InfoID.SPEED_SENSOR);
        broadcasterPower = new Broadcaster(c.getContext(), InfoID.POWER_SENSOR);
        broadcasterCadence = new Broadcaster(c.getContext(), InfoID.CADENCE_SENSOR);
        broadcasterSpeed = new Broadcaster(c.getContext(), InfoID.SPEED_SENSOR);

        name_power = c.getContext().getString(R.string.sensor_power);
        name_speed = c.getContext().getString(R.string.sensor_speed);
        name_cadence = c.getContext().getString(R.string.sensor_cadence);
    }

    @Override
    public void close()  {
        connectorPower.close();
        connectorSpeed.close();
        connectorCadence.close();
        wheelCircumference.close();
    }

    @Override
    public void changed(BluetoothGattCharacteristic c) {
        if (CYCLING_POWER_SERVICE.equals(c.getService().getUuid())) {
            if (CYCLING_POWER_MEASUREMENT.equals(c.getUuid())) {
                information = new Information(new Attributes(c));
                connectorPower.connect(true);
                connectorSpeed.connect(isSpeedSensor);
                connectorCadence.connect(isCadenceSensor);
            }
        }
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean discovered(BluetoothGattCharacteristic c, Executor execute) {
        boolean disc = false;
        if (CYCLING_POWER_SERVICE.equals(c.getService().getUuid())) {
            valid = true;
            disc = true;

            if (CYCLING_POWER_FEATURE.equals(c.getUuid())) {
                execute.read(c);

            } else if (SENSOR_LOCATION.equals(c.getUuid())) {
                execute.read(c);

            } else if (CYCLING_POWER_MEASUREMENT.equals(c.getUuid())) {
                execute.notify(c);

            }
        }
        return disc;
    }

    @Override
    public void read(BluetoothGattCharacteristic c) {
        if (CYCLING_POWER_SERVICE.equals(c.getService().getUuid())) {
            if (CYCLING_POWER_FEATURE.equals(c.getUuid())) {
                final Integer flags = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0);
                isSpeedSensor = flags != null && isFlagSet(flags, FEATURE_BIT_WHEEL_REVOLUTIONS);
                isCadenceSensor = flags != null && isFlagSet(flags, FEATURE_BIT_CRANK_REVOLUTIONS);
            } else if (SENSOR_LOCATION.equals(c.getUuid())) {
                final Integer i = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                if (i != null && i < PowerAttributes.SENSOR_LOCATION.length)
                    location = PowerAttributes.SENSOR_LOCATION[i];
            }
        }
    }

    @Override
    public GpxInformation getInformation(int iid) {
        if (iid == InfoID.POWER_SENSOR)
            return information;

        if (isSpeedSensor && iid == InfoID.SPEED_SENSOR)
            return information;

        if (isCadenceSensor && iid == InfoID.CADENCE_SENSOR)
            return information;

        return null;
    }

    @NonNull
    @Override
    public String toString() {
        String result = "";

        if (valid) {
            result = name_power;

            if (isSpeedSensor)
                result += " & " + name_speed;

            if (isCadenceSensor)
                result += " & " + name_cadence;
        }

        return result;
    }

    private class Attributes extends PowerAttributes {
        private float speedSI = 0f;

        public Attributes(BluetoothGattCharacteristic c) {
            super(CyclingPower.this.location,
                  CyclingPower.this.isCadenceSensor,
                  CyclingPower.this.isSpeedSensor);

            int offset = 0;

            final int flags = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
            offset += 2;

            int instantaneous_power = c.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16, offset);
            offset += 2;

            broadcastPower(instantaneous_power);

            if (isFlagSet(flags, BIT_PEDAL_POWER_BALANCE)) {
                ++offset;
            }

            if (isFlagSet(flags, BIT_ACCUMULATED_TORQUE)) {
                offset += 2;
            }

            if (isFlagSet(flags, BIT_WHEEL_REVOLUTION_DATA)) {
                Integer revolutions = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, offset);
                offset += 4;
                Integer time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                offset += 2;

                if (revolutions != null && time != null) {
                    speed.addUINT32(time, revolutions);
                    broadcastSpeed(speed.rpm());
                }
            }

            if (isFlagSet(flags, BIT_CRANK_REVOLUTION_DATA)) {
                Integer revolutions = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                offset += 2;
                Integer time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                // offset += 2;

                cadence.add(time, revolutions);
                broadcastCadence(cadence.rpm());
            }

            // TODO ...
        }

        private void broadcastPower(int _power) {
            if (_power != 0 || broadcasterPower.timeout()) {
                power = _power;

                broadcasterPower.broadcast();
            }
        }

        private void broadcastSpeed(int rpm) {
            if (rpm != 0 || broadcasterSpeed.timeout()) {
                circumferenceSI = wheelCircumference.getCircumferenceSI();

                if (circumferenceSI > 0f) {
                    speedSI = speed.getSpeedSI(circumferenceSI);
                }

                circumferenceDebugString = wheelCircumference.getDebugString();

                broadcasterSpeed.broadcast();
            }

        }

        private void broadcastCadence(int rpm) {
            if (rpm != 0 || broadcasterCadence.timeout()) {
                cadence_rpm = rpm;
                cadence_rpm_average = rpm;

                broadcasterCadence.broadcast();
            }
        }

        public float getSpeedSI() {
            return speedSI;
        }
    }

    private static class Information extends GpxInformation {
        private final Attributes attributes;
        private final long timeStamp = System.currentTimeMillis();

        public Information(Attributes a) {
            attributes = a;
        }

        @Override
        public GpxAttributes getAttributes() {
            return attributes;
        }

        @Override
        public long getTimeStamp() {
            return timeStamp;
        }

        @Override
        public float getSpeed() {
            return attributes.getSpeedSI();
        }
    }

    private static boolean isFlagSet(int flags, int bit) {
        return (flags & (1 << bit)) != 0;
    }
}
