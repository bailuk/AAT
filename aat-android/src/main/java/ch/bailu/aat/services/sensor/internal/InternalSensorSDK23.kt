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
import ch.bailu.aat.services.sensor.list.SensorListItem;

@RequiresApi(api = 23)
public abstract class InternalSensorSDK23 implements SensorEventListener, SensorInterface {


    private final Context context;

    private final String name;
    private final String address;

    private boolean registered = false;

    private final Connector connector;
    private final SensorListItem item;


    public InternalSensorSDK23(Context c, SensorListItem i, Sensor sensor, int iid) {
        context = c;
        item = i;
        name = InternalSensorsSDK23.toName(sensor);
        address = InternalSensorsSDK23.toAddress(sensor);
        connector = new Connector(c, iid);

        if (item.lock(this)) {
            item.setState(SensorItemState.CONNECTING);
            item.setState(SensorItemState.CONNECTED);

            connector.connect();

            requestUpdates(this, sensor);

        }
    }

    /**
     * Was this instance successfully registered with the
     * SensorListItem?
     */
    protected final boolean isLocked() {
        return item.isLocked(this);
    }

    @Override
    public String getName() {
        return name;
    }


    @NonNull
    @Override
    public String toString() {
        return getName() + "@" + address + ":" + item.getSensorStateDescription(context);
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
            manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            registered = true;
        }
    }


    private void cancelUpdates(SensorEventListener listener) {
        final SensorManager manager = context.getSystemService(SensorManager.class);

        if (registered) {
            manager.unregisterListener(listener);
            registered = false;
        }
    }
}

