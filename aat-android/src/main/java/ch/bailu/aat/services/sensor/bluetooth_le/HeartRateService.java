package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.UUID;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.gpx.attributes.SensorInformation;
import ch.bailu.aat.services.sensor.Connector;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.attributes.HeartRateAttributes;

@RequiresApi(api = 18)
public final class HeartRateService extends HeartRateServiceID implements ServiceInterface {
    /**
     *
     * EC DMH30 0ADE BBB Bluepulse+ Heart Rate Sensor BCP-62DB
     * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.service.heart_rate.xml
     *
     */

    private String location = HeartRateAttributes.BODY_SENSOR_LOCATIONS[0];
    private GpxInformation information;


    private boolean valid = false;
    private final Connector connector;
    private final Broadcaster broadcaster;

    private final String name;

    public HeartRateService(Context c) {
        connector = new Connector(c, InfoID.HEART_RATE_SENSOR);
        broadcaster = new Broadcaster(c, InfoID.HEART_RATE_SENSOR);

        name = c.getString(R.string.sensor_heart_rate);
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean discovered(BluetoothGattCharacteristic c, Executer execute) {
        UUID sid = c.getService().getUuid();
        UUID cid = c.getUuid();
        boolean disc = false;

        if (HEART_RATE_SERVICE.equals(sid)) {
            valid = true;
            disc = true;

            if (HEART_RATE_MEASUREMENT.equals(cid)) {
                execute.notify(c);
            } else if (BODY_SENSOR_LOCATION.equals(cid)) {
                execute.read(c);
            }
        }
        return disc;
    }

    @Override
    public void read(BluetoothGattCharacteristic c) {
        if (HEART_RATE_SERVICE.equals(c.getService().getUuid())) {
            if (BODY_SENSOR_LOCATION.equals(c.getUuid())) {
                readBodySensorLocation(c.getValue());
                connector.connect(isValid());
                information = new SensorInformation(new HeartRateAttributes(location));
                broadcaster.broadcast();

            }
        }
    }


    @Override
    public void changed(BluetoothGattCharacteristic c) {
        if (HEART_RATE_SERVICE.equals(c.getService().getUuid())) {

            if (HEART_RATE_MEASUREMENT.equals(c.getUuid())) {
                readHeartRateMeasurement(c, c.getValue());
            }
        }

    }

    private void readHeartRateMeasurement(BluetoothGattCharacteristic c, byte[] value) {
        information = new SensorInformation(new Attributes(c, value));


    }


    @NonNull
    @Override
    public String toString() {
        return name;
    }

    private void readBodySensorLocation(byte[] value) {

        if (value[0] < HeartRateAttributes.BODY_SENSOR_LOCATIONS.length) {
            location = HeartRateAttributes.BODY_SENSOR_LOCATIONS[value[0]];
        }
    }


    @Override
    public void close()  {
        connector.close();
        broadcaster.broadcast();
    }



    private class Attributes extends HeartRateAttributes {


        public Attributes(BluetoothGattCharacteristic c, byte[] v) {
            super(HeartRateService.this.location);
            int offset = 0;
            byte flags = v[offset];

            boolean bpmUint16 = ID.isBitSet(flags, 0);

            boolean haveSensorContactStatus = ID.isBitSet(flags, 1);
            haveSensorContact = ID.isBitSet(flags, 2);

            boolean haveEnergyExpended = ID.isBitSet(flags, 3);
            boolean haveRrIntervall = ID.isBitSet(flags, 4);

            offset += 1;

            if (bpmUint16) {
                setBpm(c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset));
                offset += 2;

            } else {
                setBpm(c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset));
                offset += 1;

            }

            if (haveEnergyExpended) {
                offset += 2;
            }

            if (haveRrIntervall) {

                rrIntervall = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);

                if (!haveBpm() && rrIntervall > 0) {
                    setBpm(Math.round (MINUTE / (float)rrIntervall));
                }
            }




            if (haveBpm()) {
                if (!haveSensorContactStatus) haveSensorContact = true;
                broadcaster.broadcast();

            } else if (haveSensorContactStatus && !haveSensorContact) {
                broadcaster.broadcast();

            } else if (broadcaster.timeout()){
                if (!haveSensorContactStatus) haveSensorContact = false;
                broadcaster.broadcast();
            }
        }
    }



    @Override
    public GpxInformation getInformation(int iid) {
        if (iid == InfoID.HEART_RATE_SENSOR)
            return information;
        return null;
    }
}
