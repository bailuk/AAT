package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import ch.bailu.aat.services.sensor.Connector;
import ch.bailu.aat.services.sensor.SensorInterface;
import ch.bailu.aat.services.sensor.list.SensorItemState;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat.services.sensor.list.SensorListItem;

@RequiresApi(api = 23)
public abstract class InternalSensorSDK23 implements SensorEventListener, SensorInterface {


    private final Context context;

    private final String name;
    private final String address;

    private boolean registered = false;

    private final Connector connector;
    private final SensorList sensorList;
    private final SensorListItem item;


    public InternalSensorSDK23(Context c, SensorList list, Sensor sensor, int iid) {
        context = c;
        sensorList = list;
        name = InternalSensorsSDK23.toName(sensor);
        address = InternalSensorsSDK23.toAddress(sensor);
        connector = new Connector(c, iid);

        item = sensorList.add(this);
        if (item.lock(this)) {
            item.setState(SensorItemState.CONNECTING);
            item.setState(SensorItemState.CONNECTED);

            connector.connect();

            requestUpdates(this, sensor);

        }
    }


    public SensorListItem getItem() {
        return item;
    }

    @Override
    public String getName() {
        return name;
    }


    @NonNull
    @Override
    public String toString() {
        return getName() + "@" + getAddress() + ":" + item.getSensorStateDescription(context);
    }



    @Override
    public void close() {
        if (item.unlock(this)) {

            connector.close();
            cancelUpdates(this);

            item.setState(SensorItemState.ENABLED);
        }
    }


    private void requestUpdates(SensorEventListener listener, Sensor sensor) {
        final SensorManager manager = context.getSystemService(SensorManager.class);

        if (manager != null) {
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

