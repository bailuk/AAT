package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.RequiresApi;

import java.util.UUID;

@RequiresApi(api = 18)
public class HeartRateService {
    /**
     *
     * EC DMH30 0ADE BBB Bluepulse+ Heart Rate Sensor BCP-62DB
     * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.service.heart_rate.xml
     *
     */

    public final static UUID HEART_RATE_SERVICE = ID.toUUID(0x180d);
    public final static UUID HEART_RATE_MESUREMENT = ID.toUUID(0x2a37);
    public final static UUID BODY_SENSOR_LOCATION = ID.toUUID(0x2a38);


    private static final String[] BODY_SENSOR_LOCATIONS = {
            "Other",
            "Chest",
            "Wrist",
            "Finger",
            "Hand",
            "Ear Lobe",
            "Foot"
    };

    private String location = BODY_SENSOR_LOCATIONS[0];


    private boolean valid = false;

    public boolean isValid() {
        return valid;
    }


    public void discovered(BluetoothGattCharacteristic c, Executer execute) {
        UUID sid = c.getService().getUuid();
        UUID cid = c.getUuid();

        if (HEART_RATE_SERVICE.equals(sid)) {
            valid = true;

            if (HEART_RATE_MESUREMENT.equals(cid)) {
                execute.notify(c);
            } else if (BODY_SENSOR_LOCATION.equals(cid)) {
                execute.read(c);
            }
        }
    }


    public void read(BluetoothGattCharacteristic c) {
        if (HEART_RATE_SERVICE.equals(c.getService().getUuid())) {

            if (BODY_SENSOR_LOCATION.equals(c.getUuid())) {
                readBodySensorLocation(c.getValue());
            }
        }
    }


    public void notify(BluetoothGattCharacteristic c) {
        if (HEART_RATE_SERVICE.equals(c.getService().getUuid())) {

            if (HEART_RATE_MESUREMENT.equals(c.getUuid())) {
                readHeartRateMesurement(c, c.getValue());
            }
        }

    }


    @Override
    public String toString() {
        return "Heart Rate Sensor [" + location + "]";
    }

    private void readBodySensorLocation(byte[] value) {

        if (value[0] < BODY_SENSOR_LOCATIONS.length) {
            location = BODY_SENSOR_LOCATIONS[value[0]];
        }
    }



    private boolean bpmUint16 = false;


    private boolean haveSensorContactStatus = false;
    private boolean haveSensorContact = false;

    private boolean haveEnergyExpended = false;
    private boolean haveRrIntervall = false;

    private int bpm = 0;
    private double rrIntervall = 0d;

    private void readHeartRateMesurement(BluetoothGattCharacteristic c, byte[] v) {
        int offset = 0;
        byte flags = v[offset];


        bpmUint16 = ID.isBitSet(flags, 0);

        haveSensorContactStatus = ID.isBitSet(flags, 1);
        haveSensorContact = ID.isBitSet(flags, 2);

        haveEnergyExpended = ID.isBitSet(flags, 3);
        haveRrIntervall = ID.isBitSet(flags, 4);

        offset += 1;

        if (bpmUint16) {
            bpm = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
            offset += 2;

        } else {
            bpm = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset);
            offset += 1;

        }

        if (haveEnergyExpended) {
            offset += 2;
        }

        if (haveRrIntervall) {
            rrIntervall = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
            rrIntervall = rrIntervall / 1024d;
        }
    }
}
