package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.RequiresApi;

import java.util.UUID;

import ch.bailu.aat.util.ui.AppLog;

@RequiresApi(api = 18)
public class HeartRateService {
    // EC DMH30 0ADE BBB Bluepulse+ Heart Rate Sensor BCP-62DB


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
                logBodySensorLocation(c.getValue());
            }
        }
    }


    public void notify(BluetoothGattCharacteristic c) {
        if (HEART_RATE_SERVICE.equals(c.getService().getUuid())) {

            if (HEART_RATE_MESUREMENT.equals(c.getUuid())) {
                logHeartRateMesurement(c, c.getValue());
            }
        }

    }


    @Override
    public String toString() {
        return "Heart Rate Sensor [" + location + "]";
    }

    private void logBodySensorLocation(byte[] value) {

        if (value[0] < BODY_SENSOR_LOCATIONS.length) {
            location = BODY_SENSOR_LOCATIONS[value[0]];
        }
        AppLog.d(this, value.length + " Body Sensor Location " + location);
    }



    private void logHeartRateMesurement(BluetoothGattCharacteristic c, byte[] v) {
        AppLog.d(this, v.length + " HeartRateMesurement");
        byte flags = v[0];

        if (ID.isBitSet(flags, 0)) {
            AppLog.d(this, "bpm UNIT8");
        } else {
            AppLog.d(this, "bpm UNIT16");
        }

        if (ID.isBitSet(flags, 1)) {

            if (ID.isBitSet(flags, 2)) {
                AppLog.d(this, "3 supported & detected");
            } else {
                AppLog.d(this, "2 supported");
            }

        } else {
            if (ID.isBitSet(flags, 2)) {
                AppLog.d(this, "1 not supported");
            } else {
                AppLog.d(this, "0 not supported");
            }
        }


        if (ID.isBitSet(flags, 3)) {
            AppLog.d(this, "Energy extension");
        } else {
            AppLog.d(this, "No energy extension");
        }

        if (ID.isBitSet(flags, 4)) {
            AppLog.d(this, "RR intervall is present");
        } else {
            AppLog.d(this, "No rr intervall");
        }
    }

}
