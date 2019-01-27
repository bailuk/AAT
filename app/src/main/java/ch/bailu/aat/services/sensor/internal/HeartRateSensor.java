package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.annotation.RequiresApi;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.sensor.Averager;
import ch.bailu.aat.services.sensor.attributes.HeartRateAttributes;
import ch.bailu.aat.services.sensor.attributes.SensorInformation;
import ch.bailu.aat.util.AppBroadcaster;

@RequiresApi(api = 23)
public class HeartRateSensor extends InternalSensorSDK23 {

    private final Context context;
    private final Averager averager = new Averager(10);

    private boolean contact = false;

    private int bpm = 0;

    private GpxInformation information = GpxInformation.NULL;


    public HeartRateSensor(Context c, Sensor sensor) {
        super(c, sensor, InfoID.HEART_RATE_SENSOR);
        context = c;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        onAccuracyChanged(event.sensor, event.accuracy);

        if (event != null && event.values.length > 0) {
            bpm = (int) event.values[0];
        }

        broadcast();
    }

    private void broadcast() {


        final HeartRateAttributes attributes = new HeartRateAttributes();

        attributes.bpm = bpm;
        attributes.haveSensorContact = contact;

        if (bpm != 0) averager.add(bpm);
        attributes.bpmAverage = averager.get();

        information = new SensorInformation(attributes);

        if (contact)
            AppBroadcaster.broadcast(context,
                AppBroadcaster.SENSOR_CHANGED + InfoID.HEART_RATE_SENSOR);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        contact = (
                accuracy != SensorManager.SENSOR_STATUS_UNRELIABLE &&
                accuracy != SensorManager.SENSOR_STATUS_NO_CONTACT
        );
    }

    @Override
    public GpxInformation getInformation(int iid) {
        if (iid == InfoID.HEART_RATE_SENSOR)
            return information;
        return null;
    }

}
