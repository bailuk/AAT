package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.support.annotation.RequiresApi;

import java.util.UUID;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.util.AppBroadcaster;

@RequiresApi(api = 18)
public class HeartRateService extends HeartRateServiceID {
    /**
     *
     * EC DMH30 0ADE BBB Bluepulse+ Heart Rate Sensor BCP-62DB
     * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.service.heart_rate.xml
     *
     */


    private String location = BODY_SENSOR_LOCATIONS[0];

    private GpxInformation information = GpxInformation.NULL;

    private final Context context;

    private boolean valid = false;

    public HeartRateService(Context c) {
        context = c;
    }

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

    private void readHeartRateMesurement(BluetoothGattCharacteristic c, byte[] value) {
        information = new Information(new Attributes(c, value));
        AppBroadcaster.broadcast(context, AppBroadcaster.BLE_DEVICE_NOTIFIED);
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



    private final Averager averager = new Averager();

    private class Attributes extends GpxAttributes {

        private boolean haveSensorContactStatus = false;
        private boolean haveSensorContact = false;

        private boolean haveEnergyExpended = false;
        private boolean haveRrIntervall = false;

        private int bpm = 0;
        private int bpma = 0;

        private float rrIntervall = 0f;



        public Attributes(BluetoothGattCharacteristic c, byte[] v) {
            int offset = 0;
            byte flags = v[offset];

            boolean bpmUint16 = ID.isBitSet(flags, 0);

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
                rrIntervall = rrIntervall / 1024f;

                if (bpm == 0 && rrIntervall > 0f) {
                    bpm = Math.round (60f / rrIntervall);
                }
            }

            if (bpm > 0) {
                averager.add(bpm);
                bpma = averager.get();
                if (!haveSensorContactStatus) haveSensorContact = true;
            } else {
                if (!haveSensorContactStatus) haveSensorContact = false;
            }


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
            if (index == BPM_KEY_INDEX) {
                return String.valueOf(bpm);

            } else if (index == BPMA_KEY_INDEX) {
                return String.valueOf(bpma);

            } else if (index == RR_KEY_INDEX) {
                return String.valueOf(rrIntervall);


            } else if (index == CONTACT_KEY_INDEX) {
                 if (haveSensorContact) return "on";
                 return "...";
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
        private final GpxAttributes attributes;
        private final long timeStamp = System.currentTimeMillis();


        public Information(GpxAttributes a) {
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
    }


    public GpxInformation getInformation() {
        return information;
    }


    private static class Averager {
        private final static int MAX_SAMPLES = 20;

        private final int values[] = new int[MAX_SAMPLES];
        private int size = 0;
        private int next = 0;

        public void add(int b) {
            values[next] = b;
            if (size < MAX_SAMPLES) size++;

            next++;
            if (next >= MAX_SAMPLES) next = 0;
        }

        public int get() {
            int r = 0;

            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    r = r + values[i];
                }

                r = r / size;
            }
            return r;
        }
    }
}
