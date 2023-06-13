package ch.bailu.aat.services.sensor;

import android.content.Context;

import java.io.Closeable;

import ch.bailu.aat_lib.service.sensor.SensorState;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.gpx.InfoID;

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
        OldAppBroadcaster.broadcast(context, changedAction);
    }


    @Override
    public void close() {
        if (connected) {
            connected = false;
            SensorState.disconnect(iid);

            // just disconnected try reconnect
            OldAppBroadcaster.broadcast(context, disconnectedAction);

        }
    }

    public boolean isConnected() {
        return connected;
    }
}
