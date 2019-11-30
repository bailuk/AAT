package ch.bailu.aat.services.location;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

public final class Barometer{

    private final Context context;


    public Barometer(Context c) {
        context = c;
    }


    public void requestUpdates(SensorEventListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final SensorManager manager = context.getSystemService(SensorManager.class);

            if (manager != null) {
                final Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_PRESSURE);

                if (sensor != null) {
                    manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        }
    }


    public void cancelUpdates(SensorEventListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final SensorManager manager = context.getSystemService(SensorManager.class);

            if (manager != null) {
                manager.unregisterListener(listener);
            }
        }
    }


    public String getSensorID() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final SensorManager manager = context.getSystemService(SensorManager.class);

            if (manager != null) {
                final Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_PRESSURE);

                if (sensor != null) {
                    return sensor.toString();
                }
            }
        }
        return null;
    }


    public static float getPressure(SensorEvent event) {
        if (event.values.length > 0) {
            return event.values[0];
        }
        return 0f;
    }

    public boolean haveSensor() {
        return getSensorID() != null;
    }
}
