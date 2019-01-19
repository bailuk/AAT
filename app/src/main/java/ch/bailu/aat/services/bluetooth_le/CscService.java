package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.RequiresApi;

import java.util.UUID;

@RequiresApi(api = 18)
public class CscService {
    /**
     *
     * RPM BBB BCP-66 SmartCadence RPM Sensor
     *
     * CSC ( Cycling Speed And Cadence)
     * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.service.cycling_speed_and_cadence.xml
     */


    public final static UUID CSC_SERVICE = ID.toUUID(0x1816);
    public final static UUID CSC_MESUREMENT = ID.toUUID(0x2A5B);
    public final static int BIT_SPEED = 0;
    public final static int BIT_CADENCE = 1;
    public final static int BIT_SPEED_AND_CADENCE = 2;

    public final static UUID CSC_FEATURE = ID.toUUID(0x2A5C);
    public final static UUID CSC_SENSOR_LOCATION = ID.toUUID(0x2A5D);
    public final static UUID CSC_CONTROL_POINT = ID.toUUID(0x2A55);


    private final static String[] SENSOR_LOCATION = {
            "Other",
            "Top of shoe",
            "In shoe",
            "Hip",
            "Front Wheel",
            "Left Crank",
            "Right Crank",
            "Left Pedal",
            "Right Pedal",
            "Front Hub",
            "Rear Dropout",
            "Chainstay",
            "Rear Wheel",
            "Rear Hub",
            "Chest",
            "Spider",
            "Chain Ring",
    };

    private String location = SENSOR_LOCATION[0];

    private boolean speed = false;
    private boolean cadence = false;

    private boolean valid = false;

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


    private int cumulative_wheel_revolutions = 0;
    private double last_wheel_event_time = 0d;

    private int cumulative_crank_revolutions = 0;
    private double last_crank_event_time = 0d;

    private long timestamp = 0;

    private void readCscMesurement(BluetoothGattCharacteristic c, byte[] value) {
        int offset = 0;

        timestamp = System.currentTimeMillis();

        byte data = value[offset];

        offset += 1;

        if (ID.isBitSet(data, BIT_SPEED)) {
            cumulative_wheel_revolutions =
                    c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, offset);
            offset += 4;

            last_wheel_event_time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
            last_wheel_event_time /= 1024d;
            offset += 2;

        }


        if (ID.isBitSet(data, BIT_CADENCE)) {
            cumulative_crank_revolutions = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
            offset += 2;

            last_crank_event_time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
            last_crank_event_time /= 1024d;
        }
    }



    private void readCscFeature(byte[] v) {
        if (v.length > 0) {
            byte b = v[0];

            if (ID.isBitSet(b, BIT_SPEED)) {
                speed = true;

            } else if (ID.isBitSet(b, BIT_CADENCE)) {
                cadence = true;

            } else if (ID.isBitSet(b, BIT_SPEED_AND_CADENCE)) {
                speed = true;
                cadence = true;
            }
        }
    }


    @Override
    public String toString() {
        String name = "";

        if (!valid) {
            name = "No ";
        }

        if (speed) {
            name = "Speed ";
        }

        if (cadence) {
            name += "Cadence ";
        }

        return name + "Sensor [" + location + "]";
    }


}
