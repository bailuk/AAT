package ch.bailu.aat.services.sensor.bluetooth_le;

import android.content.Context;

import ch.bailu.aat.util.AppBroadcaster;

public class Broadcaster {
    private static final long BROADCAST_TIMEOUT = 2000;

    private final Context context;
    private final int IID;

    private long lastBroadcast=0L;


    public Broadcaster(Context c, int iid) {
        context = c;
        IID = iid;
    }


    public boolean timeout() {
        return (System.currentTimeMillis() - lastBroadcast) > BROADCAST_TIMEOUT;
    }


    public void broadcast() {
        lastBroadcast = System.currentTimeMillis();
        AppBroadcaster.broadcast(context, AppBroadcaster.SENSOR_CHANGED + IID);
    }
}
