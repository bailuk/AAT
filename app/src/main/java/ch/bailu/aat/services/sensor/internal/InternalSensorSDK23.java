package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.RequiresApi;

import ch.bailu.aat.services.sensor.SensorInterface;
import ch.bailu.aat.services.sensor.Connector;

@RequiresApi(api = 23)
public abstract class InternalSensorSDK23 implements SensorEventListener, SensorInterface {


    private final Context context;

    private final String name;
    private final String address;

    private boolean registered = false;

    private final Connector connector;

    public InternalSensorSDK23(Context c, Sensor sensor, int iid) {
        context = c;
        name = sensor.getVendor() + " " + sensor.getName();
        address = InternalSensorsSDK23.toAddress(sensor);
        requestUpdates(this, sensor);

        connector = new Connector(c, iid);
        connector.connect();
    }



    @Override
    public String getName() {
        return toString();
    }


    @Override
    public String toString() {
        return name;
    }


    @Override
    public boolean isConnectionEstablished() {
        return connector.isConnectionEstablished();
    }


    @Override
    public void close() {
        connector.close();
        cancelUpdates(this);

    }


    private void requestUpdates(SensorEventListener listener, Sensor sensor) {
        final SensorManager manager = context.getSystemService(SensorManager.class);

        if (manager instanceof SensorManager) {
            if (sensor instanceof android.hardware.Sensor) {

                manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                registered = true;
            }
        }
    }


    private void cancelUpdates(SensorEventListener listener) {
        final SensorManager manager = context.getSystemService(SensorManager.class);

        if (registered && manager instanceof SensorManager) {
            manager.unregisterListener(listener);
            registered = false;
        }
    }


    @Override
    public String getAddress() {
        return address;
    }
}

