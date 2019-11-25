package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.annotation.RequiresApi;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.sensor.attributes.HeartRateAttributes;
import ch.bailu.aat.services.sensor.attributes.SensorInformation;
import ch.bailu.aat.services.sensor.bluetooth_le.Broadcaster;
import ch.bailu.aat.services.sensor.list.SensorList;

@RequiresApi(api = 23)
public final class HeartRateSensor extends InternalSensorSDK23 {

    private boolean contact = false;
    private int bpm = 0;

    private GpxInformation information;

    private final Broadcaster broadcaster;

    public HeartRateSensor(Context c, SensorList list, Sensor sensor) {
        super(c, list, sensor, InfoID.HEART_RATE_SENSOR);
        broadcaster = new Broadcaster(c, InfoID.HEART_RATE_SENSOR);

        information = factoryInformation();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        contact = toContact(event);
        bpm = toBpm(event);
        information = factoryInformation();

        if (bpm > 0) {
            broadcaster.broadcast();

        } else if (contact == false) {
            broadcaster.broadcast();

        } else if (broadcaster.timeout()) {
            broadcaster.broadcast();
        }
    }





    private int toBpm(SensorEvent event) {
        if (event != null && event.values.length > 0) {
            return (int) event.values[0];
        }
        return 0;
    }


    private static boolean toContact(SensorEvent event) {
        return ( event != null && toContact(event.accuracy) );

    }

    private static boolean toContact(int accuracy) {
        return (accuracy != SensorManager.SENSOR_STATUS_UNRELIABLE &&
                accuracy != SensorManager.SENSOR_STATUS_NO_CONTACT);

    }

    private GpxInformation factoryInformation() {
        final HeartRateAttributes attributes = new HeartRateAttributes();

        attributes.bpm = bpm;
        attributes.haveSensorContact = contact;

        return new SensorInformation(attributes);
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        contact = toContact(accuracy);
        broadcaster.broadcast();
    }


    @Override
    public GpxInformation getInformation(int iid) {
        if (iid == InfoID.HEART_RATE_SENSOR)
            return information;
        return null;
    }

}
