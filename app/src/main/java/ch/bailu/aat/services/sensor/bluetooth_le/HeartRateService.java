package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.support.annotation.RequiresApi;

import java.util.UUID;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.sensor.Averager;
import ch.bailu.aat.services.sensor.attributes.HeartRateAttributes;
import ch.bailu.aat.services.sensor.attributes.SensorInformation;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ToDo;

@RequiresApi(api = 18)
public class HeartRateService extends HeartRateServiceID {
    /**
     *
     * EC DMH30 0ADE BBB Bluepulse+ Heart Rate Sensor BCP-62DB
     * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.service.heart_rate.xml
     *
     */


    private String location = HeartRateAttributes.BODY_SENSOR_LOCATIONS[0];

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
        information = new SensorInformation(new Attributes(c, value));
        AppBroadcaster.broadcast(context, AppBroadcaster.SENSOR_CHANGED + InfoID.HEART_RATE_SENSOR);
    }


    @Override
    public String toString() {
        return ToDo.translate("Heart Rate Sensor");
    }

    private void readBodySensorLocation(byte[] value) {

        if (value[0] < HeartRateAttributes.BODY_SENSOR_LOCATIONS.length) {
            location = HeartRateAttributes.BODY_SENSOR_LOCATIONS[value[0]];
        }
    }



    private final Averager averager = new Averager(10);

    private class Attributes extends HeartRateAttributes {

        private boolean haveSensorContactStatus = false;
        private boolean haveEnergyExpended = false;
        private boolean haveRrIntervall = false;


        public Attributes(BluetoothGattCharacteristic c, byte[] v) {
            super(HeartRateService.this.location);
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

                if (bpm == 0 && rrIntervall > 0) {
                    bpm = Math.round (MINUTE / rrIntervall);
                }
            }

            if (bpm > 0) {
                averager.add(bpm);
                bpmAverage = averager.get();
                if (!haveSensorContactStatus) haveSensorContact = true;
            } else {
                if (!haveSensorContactStatus) haveSensorContact = false;
            }



        }
    }


    public GpxInformation getInformation(int iid) {
        if (iid == InfoID.HEART_RATE_SENSOR)
            return information;
        return null;
    }
}
