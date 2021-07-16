package ch.bailu.aat.services.sensor;

import android.content.Context;

import java.io.Closeable;

import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.sensor.list.SensorState;
import ch.bailu.aat.util.AppBroadcaster;

public final class Connector implements Closeable {
    private static final String changedAction = AppBroadcaster.SENSOR_CHANGED + InfoID.SENSORS;
    private static final String disconnectedAction = AppBroadcaster.SENSOR_DISCONNECTED + InfoID.SENSORS;

    private boolean connected = false;
    private final int iid;
    private final Context context;



    public Connector(Context c, int i) {
        iid = i;
        context = c;
    }

    public void connect() {
        if (!connected) {
            connected = true;
            SensorState.connect(iid);
            broadcast();
        }

    }


    public void connect(boolean condition) {
        if (condition) {
            connect();
        }
    }


    private void broadcast() {
        AppBroadcaster.broadcast(context, changedAction);
    }


    @Override
    public void close() {
        if (connected) {
            connected = false;
            SensorState.disconnect(iid);

            // just disconnected try reconnect
            AppBroadcaster.broadcast(context, disconnectedAction);

        }
    }

    public boolean isConnected() {
        return connected;
    }
}
