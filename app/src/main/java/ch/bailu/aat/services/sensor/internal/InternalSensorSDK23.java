package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.RequiresApi;

import ch.bailu.aat.services.sensor.Connector;
import ch.bailu.aat.services.sensor.SensorInterface;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat.services.sensor.list.SensorListItem;
import ch.bailu.aat.services.sensor.list.SensorStateID;

@RequiresApi(api = 23)
public abstract class InternalSensorSDK23 implements SensorEventListener, SensorInterface {


    private final Context context;

    private final String name;
    private final String address;

    private boolean registered = false;

    private final Connector connector;

    private final SensorList sensorList;


    public InternalSensorSDK23(Context c, SensorList list, Sensor sensor, int iid) {
        context = c;
        sensorList = list;
        name = sensor.getVendor() + " " + sensor.getName();
        address = InternalSensorsSDK23.toAddress(sensor);
        connector = new Connector(c, iid);

        SensorListItem item = getItem();
        if (item.lock(this)) {
            item.setState(SensorStateID.CONNECTING);
            item.setState(SensorStateID.CONNECTED);

            connector.connect();

            requestUpdates(this, sensor);

        }
    }


    public SensorListItem getItem() {
        return sensorList.add(this);
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
    public boolean isConnected() {
        return connector.isConnected();
    }


    @Override
    public void close() {
        SensorListItem item = getItem();

        if (item.unlock(this)) {

            connector.close();
            cancelUpdates(this);

            item.setState(SensorStateID.ENABLED);
        }
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

