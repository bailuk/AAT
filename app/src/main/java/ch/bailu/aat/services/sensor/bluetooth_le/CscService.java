package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.support.annotation.RequiresApi;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.Averager;
import ch.bailu.aat.util.AppBroadcaster;

@RequiresApi(api = 18)
public class CscService extends CscServiceID implements Closeable {
    /**
     *
     * RPM BBB BCP-66 SmartCadence RPM Sensor
     *
     * CSC (Cycling Speed And Cadence)
     * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.service.cycling_speed_and_cadence.xml
     * https://developer.polar.com/wiki/Cycling_Speed_%26_Cadence
     */

    private static final long BROADCAST_TIMEOUT = 3000;


    private long lastBroadcast = 0L;

    private String location = SENSOR_LOCATION[0];

    private boolean isSpeedSensor = false;
    private boolean isCadenceSensor = false;

    private final Revolution cadence = new Revolution();
    private final Revolution speed = new Revolution();
    private final Averager averageCadence = new Averager(5);
    private final NewWheelCircumference wheelCircumference;


    private GpxInformation information = GpxInformation.NULL;

    private final Context context;

    private boolean valid = false;

    public CscService(ServiceContext c) {
        context = c.getContext();
        wheelCircumference = new NewWheelCircumference(c, speed);
    }


    public boolean isValid() {
        return valid;
    }


    public void notify(BluetoothGattCharacteristic c) {
        if (CSC_SERVICE.equals(c.getService().getUuid())) {
            if (CSC_MESUREMENT.equals(c.getUuid())) {
                readCscMesurement(c, c.getValue());
            }
        }
    }


    public void discovered(BluetoothGattCharacteristic c, Executer execute) {
        if (CSC_SERVICE.equals(c.getService().getUuid())) {
            valid = true;

            if (CSC_FEATURE.equals(c.getUuid())) {
                execute.read(c);

            } else if (CSC_SENSOR_LOCATION.equals(c.getUuid())) {
                execute.read(c);

            } else if (CSC_MESUREMENT.equals(c.getUuid())) {
                execute.notify(c);

            }
        }
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
        if (v.length > 0 && v[0] < SENSOR_LOCATION.length) {
            location = SENSOR_LOCATION[v[0]];
        }
    }




    private void readCscMesurement(BluetoothGattCharacteristic c, byte[] value) {
        information = new Information(new Attributes(c, value));
    }



    private void readCscFeature(byte[] v) {
        if (v.length > 0) {
            byte b = v[0];

            if (ID.isBitSet(b, BIT_SPEED)) {
                isSpeedSensor = true;

            } else if (ID.isBitSet(b, BIT_CADENCE)) {
                isCadenceSensor = true;

            } else if (ID.isBitSet(b, BIT_SPEED_AND_CADENCE)) {
                isSpeedSensor = true;
                isCadenceSensor = true;
            }
        }
    }


    @Override
    public String toString() {
        String name = "";

        if (!valid) {
            name = "No ";
        }

        if (isSpeedSensor) {
            name = "Speed ";
        }

        if (isCadenceSensor) {
            name += "Cadence ";
        }

        return name + "Sensor [" + location + "]";
    }


    @Override
    public void close()  {
        wheelCircumference.close();
    }


    private class Attributes extends GpxAttributes {

        boolean haveCadence, haveSpeed;
        private int speed_rpm = 0;
        private int cadence_rpm = 0;
        private int cadence_rpm_average = 0;
        private float speedSI = 0f;
        private float circumferenceSI = 0f;


        public Attributes(BluetoothGattCharacteristic c, byte[] v) {
            int offset = 0;

            byte data = v[offset];
            offset += 1;

            haveCadence = ID.isBitSet(data, BIT_CADENCE);
            haveSpeed = ID.isBitSet(data, BIT_SPEED);

            if (haveSpeed) {
                long revolutions = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, offset);
                offset += 4;

                int time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);

                offset += 2;

                speed.addUINT32(time, revolutions);

                speed_rpm = speed.rpm();

                circumferenceSI = wheelCircumference.getCircumferenceSI();

                if (circumferenceSI > 0f) {
                    speedSI = speed.getSpeedSI(circumferenceSI);
                }
            }


            if (haveCadence) {
                int revolutions = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                offset += 2;

                int time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);

                cadence.add(time, revolutions);
                cadence_rpm = cadence.rpm();

                if (cadence_rpm != 0) {
                    averageCadence.add(cadence_rpm);
                }
            }
            cadence_rpm_average = averageCadence.get();

            broadcast();
        }


        private void broadcast() {
            final long time = System.currentTimeMillis();

            if (haveSpeed && isSpeedSensor && (timeout(time) || speed_rpm != 0)) {
                AppBroadcaster.broadcast(
                        context, AppBroadcaster.SENSOR_CHANGED + InfoID.SPEED_SENSOR);

                lastBroadcast = time;
            }

            if (haveCadence && isCadenceSensor) {
                AppBroadcaster.broadcast(
                        context, AppBroadcaster.SENSOR_CHANGED + InfoID.CADENCE_SENSOR);
            }


        }

        private boolean timeout(long time) {
            return ((time - lastBroadcast) > BROADCAST_TIMEOUT);
        }


        public float getSpeedSI() {
            return speedSI;
        }

        @Override
        public String get(String key) {
            for (int i = 0; i< KEYS.length; i++) {
                if (key.equalsIgnoreCase(KEYS[i])) return getValue(i);
            }

            return null;
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
                return String.valueOf(cadence_rpm);

            } else if (index == KEY_INDEX_CRANK_RPM_AVERAGE) {
                return String.valueOf(cadence_rpm_average);

            } else if (index == KEY_INDEX_WHEEL_CIRCUMFERENCE) {
                return String.valueOf(circumferenceSI);
            }


            return NULL_VALUE;
        }

        @Override
        public String getKey(int index) {
            if (index < KEYS.length) return KEYS[index];
            return null;
        }

        @Override
        public void put(String key, String value) {

        }

        @Override
        public int size() {
            return KEYS.length;
        }

        @Override
        public void remove(String key) {

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


    public GpxInformation getInformation(int iid) {
        if (isSpeedSensor && iid == InfoID.SPEED_SENSOR)
            return information;

        else if (isCadenceSensor && iid == InfoID.CADENCE_SENSOR) {
            return information;
        }

        return null;
    }
}
