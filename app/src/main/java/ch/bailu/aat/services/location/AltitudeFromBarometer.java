package ch.bailu.aat.services.location;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class AltitudeFromBarometer extends LocationStackChainedItem implements SensorEventListener {
    private final Hypsometric hypsometric = new Hypsometric();
    private final Barometer barometer;


    public AltitudeFromBarometer(LocationStackItem n, Context c) {
        super(n);
        barometer = new Barometer(c);
        barometer.requestUpdates(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        hypsometric.setPressure(Barometer.getPressure(event));
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    @Override
    public void close() {
        barometer.cancelUpdates(this);
    }



    @Override
    public void passLocation(LocationInformation l) {

        if (!hypsometric.isPressureAtSeaLevelValid()) {
            hypsometric.setAltitude(l.getAltitude());
        }

        if (hypsometric.isValid()) {
            l.setAltitude(hypsometric.getAltitude());
        }

        super.passLocation(l);
    }
}
