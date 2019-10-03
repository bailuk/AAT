package ch.bailu.aat.services.sensor;

import android.content.Context;

import java.io.Closeable;

import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.sensor.list.SensorState;
import ch.bailu.aat.util.AppBroadcaster;

public class Connector implements Closeable {
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
        AppBroadcaster.broadcast(context, AppBroadcaster.SENSOR_CHANGED + InfoID.SENSORS);
    }


    @Override
    public void close() {
        if (connected) {
            connected = false;
            SensorState.disconnect(iid);
            AppBroadcaster.broadcast(context, AppBroadcaster.SENSOR_DISCONECTED + InfoID.SENSORS);      // just disconected try reconnect
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
