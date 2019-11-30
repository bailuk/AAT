
package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.Connector;
import ch.bailu.aat.services.sensor.attributes.CadenceSpeedAttributes;
import ch.bailu.aat.services.sensor.list.SensorState;

@RequiresApi(api = 18)
public final class CscService extends CscServiceID implements ServiceInterface {
    /**
     *
     * RPM BBB BCP-66 SmartCadence RPM Sensor
     *
     * CSC (Cycling Speed And Cadence)
     * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.service.cycling_speed_and_cadence.xml
     * https://developer.polar.com/wiki/Cycling_Speed_%26_Cadence
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



    public CscService(ServiceContext c) {
        wheelCircumference = new WheelCircumference(c, speed);
        connectorCadence = new Connector(c.getContext(), InfoID.CADENCE_SENSOR);
        connectorSpeed = new Connector(c.getContext(), InfoID.SPEED_SENSOR);
        broadcasterCadence = new Broadcaster(c.getContext(), InfoID.CADENCE_SENSOR);
        broadcasterSpeed = new Broadcaster(c.getContext(), InfoID.SPEED_SENSOR);

    }


    public boolean isValid() {
        return valid;
    }



    @Override
    public void changed(BluetoothGattCharacteristic c) {
        if (CSC_SERVICE.equals(c.getService().getUuid())) {
            if (CSC_MESUREMENT.equals(c.getUuid())) {
                readCscMesurement(c, c.getValue());
            }
        }
    }


    public boolean discovered(BluetoothGattCharacteristic c, Executer execute) {
        boolean disc = false;
        if (CSC_SERVICE.equals(c.getService().getUuid())) {
            valid = true;
            disc = true;

            if (CSC_FEATURE.equals(c.getUuid())) {
                execute.read(c);

            } else if (CSC_SENSOR_LOCATION.equals(c.getUuid())) {
                execute.read(c);

            } else if (CSC_MESUREMENT.equals(c.getUuid())) {
                execute.notify(c);

            }
        }
        return disc;
    }

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




    private void readCscMesurement(BluetoothGattCharacteristic c, byte[] value) {
        information = new Information(new Attributes(c, value));
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
        String name = "";

        if (!valid) {
            name = "No ";
        }

        if (isSpeedSensor) {
            name = SensorState.getName(InfoID.SPEED_SENSOR) + " ";
        }

        if (isCadenceSensor) {
            name += SensorState.getName(InfoID.CADENCE_SENSOR) + " ";
        }

        return name + "Sensor";
    }


    @Override
    public void close()  {
        connectorSpeed.close();
        connectorCadence.close();
        wheelCircumference.close();
    }


    private class Attributes extends CadenceSpeedAttributes {

        private float speedSI = 0f;


        public Attributes(BluetoothGattCharacteristic c, byte[] v) {
            super(CscService.this.location, CscService.this.isCadenceSensor, CscService.this.isSpeedSensor);

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

                speed.addUINT32(time, revolutions);
                broadcastSpeed(speed.rpm());
            }


            if (haveCadence) {
                int revolutions = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                offset += 2;

                int time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);

                cadence.add(time, revolutions);
                broadcastCadence(cadence.rpm());
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
