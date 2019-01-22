package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.sensor.Sensors;


@RequiresApi(api = 23)
public class InternalSensorsSDK23 extends Sensors {
    private final ArrayList<AbsSensorSDK23> sensors = new ArrayList<>(5);


    public InternalSensorsSDK23(Context context) {
        final SensorManager manager = context.getSystemService(SensorManager.class);


        if (manager instanceof SensorManager) {
            List<Sensor> heartRateSensors = manager.getSensorList(Sensor.TYPE_HEART_RATE);

            if (heartRateSensors != null) {
                for (Sensor sensor : heartRateSensors) {
                    sensors.add(new HeartRateSensor(context, sensor));
                }
            }
        }
    }



    @Override
    public synchronized String toString() {
        String s = "";
        //String nl = "";

        for (AbsSensorSDK23 sensor : sensors) {
            if (sensor.isValid()) {
                s = s + sensor.toString() + "\n";
            }
        }
        return s;
    }


    @Override
    public synchronized GpxInformation getInformation(int iid) {
        for (AbsSensorSDK23 sensor : sensors) {
            if (sensor.isValid()) {
                GpxInformation information = sensor.getInformation(iid);
                if (information != null) return information;
            }
        }
        return null;
    }

    @Override
    public void close() {
        for (AbsSensorSDK23 sensor : sensors) sensor.close();
    }
}
