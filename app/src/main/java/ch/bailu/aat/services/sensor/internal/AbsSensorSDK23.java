package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.RequiresApi;

import ch.bailu.aat.services.sensor.SensorInterface;
import ch.bailu.aat.util.ToDo;

@RequiresApi(api = 23)
public abstract class AbsSensorSDK23 implements SensorEventListener, SensorInterface {


    private final Context context;

    private String name = "";
    private String vendor = "";


    private boolean haveSensor = false;


    public AbsSensorSDK23(Context c, Sensor sensor) {
        context = c;
        requestUpdates(this, sensor);
    }


    @Override
    public boolean isValid() {
        return haveSensor;
    }

    @Override
    public String toString() {
        if (haveSensor)
            return vendor + " " + name;

        return ToDo.translate("no sensor");
    }




    @Override
    public void close() {
        cancelUpdates(this);
    }


    public void requestUpdates(SensorEventListener listener, Sensor sensor) {
        final SensorManager manager = context.getSystemService(SensorManager.class);

        if (manager instanceof SensorManager) {
            if (sensor instanceof android.hardware.Sensor) {
                vendor = sensor.getVendor();
                name = sensor.getName();
                haveSensor = true;

                manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }


    public void cancelUpdates(SensorEventListener listener) {
        final SensorManager manager = context.getSystemService(SensorManager.class);

        if (manager instanceof SensorManager) {
            manager.unregisterListener(listener);
        }
    }

}

