
package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;

import androidx.annotation.NonNull;

import ch.bailu.aat.R;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.Connector;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.attributes.CadenceSpeedAttributes;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;

public final class CscService extends CscServiceID implements ServiceInterface {
    /**
     *
     * RPM BBB BCP-66 SmartCadence RPM Sensor
     * <p>
     * CSC (Cycling Speed And Cadence)
     * <a href="https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.service.cycling_speed_and_cadence.xml">...</a>
     * <a href="https://developer.polar.com/wiki/Cycling_Speed_%26_Cadence">...</a>
     */

    private String location = CadenceSpeedAttributes.SENSOR_LOCATION[0];

    private boolean isSpeedSensor = false;
    private boolean isCadenceSensor = false;

    private final Revolution cadence = new Revolution();
    private final Revolution speed = new Revolution();
    private final WheelCircumference wheelCircumference;


    private GpxInformation information = GpxInformation.NULL;

    private boolean valid = false;

    private final Connector connectorSpeed, connectorCadence;
    private final Broadcaster broadcasterSpeed, broadcasterCadence;

    private final String name_speed, name_cadence;

    public CscService(ServiceContext c) {
        wheelCircumference = new WheelCircumference(c, speed);
        connectorCadence = new Connector(c.getContext(), InfoID.CADENCE_SENSOR);
        connectorSpeed = new Connector(c.getContext(), InfoID.SPEED_SENSOR);
        broadcasterCadence = new Broadcaster(c.getContext(), InfoID.CADENCE_SENSOR);
        broadcasterSpeed = new Broadcaster(c.getContext(), InfoID.SPEED_SENSOR);

        name_speed = c.getContext().getString(R.string.sensor_speed);
        name_cadence = c.getContext().getString(R.string.sensor_cadence);
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void changed(BluetoothGattCharacteristic c) {
        if (CSC_SERVICE.equals(c.getService().getUuid())) {
            if (CSC_MEASUREMENT.equals(c.getUuid())) {
                readCscMeasurement(c, c.getValue());
            }
        }
    }

    @Override
    public boolean discovered(BluetoothGattCharacteristic c, Executor execute) {
        boolean disc = false;
        if (CSC_SERVICE.equals(c.getService().getUuid())) {
            valid = true;
            disc = true;

            if (CSC_FEATURE.equals(c.getUuid())) {
                execute.read(c);

            } else if (CSC_SENSOR_LOCATION.equals(c.getUuid())) {
                execute.read(c);

            } else if (CSC_MEASUREMENT.equals(c.getUuid())) {
                execute.notify(c);

            }
        }
        return disc;
    }

    @Override
    public void read(BluetoothGattCharacteristic c) {
        if (CSC_SERVICE.equals(c.getService().getUuid())) {
            if (CSC_FEATURE.equals(c.getUuid())) {
                readCscFeature(c.getValue());

            } else if (CSC_SENSOR_LOCATION.equals(c.getUuid())) {
                readCscSensorLocation(c.getValue());

            }
        }
    }

    private void readCscSensorLocation(byte[] v) {
        if (v.length > 0 && v[0] < CadenceSpeedAttributes.SENSOR_LOCATION.length) {
            location = CadenceSpeedAttributes.SENSOR_LOCATION[v[0]];
        }
    }

    private void readCscMeasurement(BluetoothGattCharacteristic c, byte[] value) {
        information = new Information(new Attributes(this, c, value));
        connectorSpeed.connect(isSpeedSensor);
        connectorCadence.connect(isCadenceSensor);
    }

    private void readCscFeature(byte[] v) {
        if (v.length > 0) {
            byte b = v[0];
            isCadenceSensor = ID.isBitSet(b, BIT_CADENCE);
            isSpeedSensor = ID.isBitSet(b, BIT_SPEED);
        }
    }


    @NonNull
    @Override
    public String toString() {
        String result = "";

        if (valid) {
            if (isSpeedSensor && isCadenceSensor) {
                result =  name_speed + " & " + name_cadence;
            } else if (isSpeedSensor) {
                result = name_speed;
            } else if (isCadenceSensor) {
                result = name_cadence;
            }
        }

        return result;
    }


    @Override
    public void close()  {
        connectorSpeed.close();
        connectorCadence.close();
        wheelCircumference.close();
    }


    private static class Attributes extends CadenceSpeedAttributes {

        private float speedSI = 0f;


        public Attributes(CscService parent, BluetoothGattCharacteristic c, byte[] v) {
            super(parent.location, parent.isCadenceSensor, parent.isSpeedSensor);

            int offset = 0;

            byte data = v[offset];
            offset += 1;

            final boolean haveCadence = ID.isBitSet(data, BIT_CADENCE);
            final boolean haveSpeed = ID.isBitSet(data, BIT_SPEED);

            if (haveSpeed) {
                long revolutions = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, offset);
                offset += 4;

                int time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);

                offset += 2;

                parent.speed.addUINT32(time, revolutions);
                broadcastSpeed(parent.broadcasterSpeed, parent.speed,
                               parent.wheelCircumference);
            }


            if (haveCadence) {
                int revolutions = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                offset += 2;

                int time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);

                parent.cadence.add(time, revolutions);
                broadcastCadence(parent.broadcasterCadence, parent.cadence.rpm());
            }
        }


        private void broadcastSpeed(Broadcaster broadcasterSpeed,
                                    Revolution speed,
                                    WheelCircumference wheelCircumference) {
            if (speed.rpm() != 0 || broadcasterSpeed.timeout()) {

                circumferenceSI = wheelCircumference.getCircumferenceSI();

                if (circumferenceSI > 0f) {
                    speedSI = speed.getSpeedSI(circumferenceSI);
                }

                circumferenceDebugString = wheelCircumference.getDebugString();

                broadcasterSpeed.broadcast();
            }

        }

        private void broadcastCadence(Broadcaster broadcasterCadence, int rpm) {
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


    @Override
    public GpxInformation getInformation(int iid) {
        if (isSpeedSensor && iid == InfoID.SPEED_SENSOR)
            return information;

        else if (isCadenceSensor && iid == InfoID.CADENCE_SENSOR) {
            return information;
        }

        return null;
    }
}
