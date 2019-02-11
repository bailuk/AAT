package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.support.annotation.RequiresApi;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.sensor.attributes.SensorInformation;
import ch.bailu.aat.services.sensor.attributes.StepCounterAttributes;
import ch.bailu.aat.services.sensor.bluetooth_le.Broadcaster;
import ch.bailu.aat.services.sensor.list.SensorList;

@RequiresApi(api = 23)
public class StepCounterSensor extends InternalSensorSDK23 {
    private static final int SAMPLES=25;

    private Sample first = Sample.NULL;
    private Sample[] samples = new Sample[SAMPLES];
    private int index = 0;

    private final Broadcaster broadcaster;
    private GpxInformation information = null;



    public StepCounterSensor(Context c, SensorList list, Sensor sensor) {
        super(c, list, sensor, InfoID.STEP_COUNTER_SENSOR);

        for (int i = 0; i< samples.length; i++) {
            samples[i] = Sample.NULL;
        }

        broadcaster = new Broadcaster(c, InfoID.SPEED_SENSOR);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        StepCounterAttributes attr = new StepCounterAttributes();

        addSample(event);
        setAttributes(attr);

        information = new SensorInformation(attr);
        broadcaster.broadcast();
    }


    private void setAttributes(StepCounterAttributes attr) {
        attr.stepsTotal = getTotalSteps();
        attr.stepsRate = getStepsRate();
    }


    private int getTotalSteps() {
        return samples[index].steps - first.steps;
    }


    private void addSample(SensorEvent event) {
        index++;
        index = index % samples.length;

        samples[index] = new Sample(event);

        if (first == Sample.NULL) {
            first = samples[index];
        }
    }


    private Sample getFirstSample() {
        int index = this.index +1;

        for (int i = 0; i< samples.length; i++) {
            index = index % samples.length;

            if (samples[index] != Sample.NULL) {
                return samples[index];
            }
        }
        return Sample.NULL;
    }


    private int getStepsRate() {
        Sample a = getFirstSample();
        Sample b = samples[index];

        long timeDelta = b.time - a.time;
        int steps = b.steps - a.steps;

        if (timeDelta > 0 && steps > 0) {
            return Math.round((steps * 1000 * 60)  / timeDelta);
        }
        return 0;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public GpxInformation getInformation(int iid) {
        if (iid == InfoID.STEP_COUNTER_SENSOR)
            return information;

        return null;
    }


    private static class Sample {
        private static final Sample NULL = new Sample();

        public final long time;
        public final int steps;

        public Sample(SensorEvent event) {
            time = event.timestamp;
            steps = (int) event.values[0];
        }

        private Sample() {
            time = 0;
            steps = 0;
        }
    }
}
