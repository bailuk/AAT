package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import androidx.annotation.RequiresApi;

import ch.bailu.aat.gpx.attributes.SensorInformation;
import ch.bailu.aat.services.sensor.bluetooth_le.Broadcaster;
import ch.bailu.aat.services.sensor.list.SensorListItem;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.attributes.HeartRateAttributes;

@RequiresApi(api = 23)
public final class HeartRateSensor extends InternalSensorSDK23 {

    private final HeartRateAttributes attributes;
    private GpxInformation information;

    private final Broadcaster broadcaster;


    public HeartRateSensor(Context c, SensorListItem item, Sensor sensor) {
        super(c, item, sensor, InfoID.HEART_RATE_SENSOR);

        broadcaster = new Broadcaster(c, InfoID.HEART_RATE_SENSOR);

        attributes = new HeartRateAttributes();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        attributes.setBpm(toBpm(event));
        attributes.haveSensorContact = toContact(event);
        information = new SensorInformation(attributes);

        if (attributes.haveBpm()) {
            broadcaster.broadcast();

        } else if (!attributes.haveSensorContact) {
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        attributes.haveSensorContact = toContact(accuracy);
        broadcaster.broadcast();
    }


    @Override
    public GpxInformation getInformation(int iid) {
        if (iid == InfoID.HEART_RATE_SENSOR)
            return information;
        return null;
    }
}
