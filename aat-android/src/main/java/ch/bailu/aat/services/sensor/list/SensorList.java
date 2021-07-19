package ch.bailu.aat.services.sensor.list;

import android.content.Context;

import java.io.Closeable;
import java.util.ArrayList;

import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.attributes.SensorStateAttributes;

public final class SensorList extends ArrayList<SensorListItem> implements Closeable {

    private static final String changedAction = AppBroadcaster.SENSOR_CHANGED + InfoID.SENSORS;

    private final Context context;

    public SensorList(Context c) {
        super(10);
        context = c;
        restore();
    }


    public SensorListItem add(String address, String name) {
        return add(address, name, SensorListItem.UNSCANNED);
    }


    public SensorListItem addEnabled(String address, String name) {
        return add(address, name, SensorListItem.ENABLED);
    }

    private SensorListItem add(String address, String name, int initialState) {
        SensorListItem item = find(address);

        if (item == null) {
            item = new SensorListItem(context, address, name, initialState);
            add(item);

        }

        return item;

    }

    public SensorListItem find(String address) {
        for (SensorListItem i : this) {
            if (i.getAddress().equalsIgnoreCase(address)) return i;
        }
        return null;
    }


    public void broadcast() {
        OldAppBroadcaster.broadcast(context, changedAction);
    }


    public GpxInformation getInformation(int iid) {
        GpxInformation i = null;

        if (iid == InfoID.SENSORS)
            return new Information(this);

        for (SensorListItem item: this) {
            i = item.getInformation(iid);
            if (i != null) return i;

        }

        return i;
    }


    @Override
    public void close()  {
        save();
        closeConnections();
    }


    public void closeConnections() {
        for (SensorListItem i: this) {
            i.close();
        }
    }


    private void save() {
        SensorListDb.write(context, this);
    }


    private void restore() {
        SensorListDb.read(context, this);
    }



    public static class Information extends GpxInformation {
        private int state = StateID.OFF;

        private final SensorStateAttributes attributes;

        public Information(Iterable<SensorListItem> list) {
            int sensorCount = 0;
            for (SensorListItem i : list) {
                if (i.isConnected()) {
                    sensorCount++;
                } else if (i.isConnecting()) {
                    state = StateID.WAIT;
                }
            }

            if (state != StateID.WAIT && sensorCount > 0)
                state = StateID.ON;

            attributes = new SensorStateAttributes(sensorCount);
        }


        @Override
        public int getState() {
            return state;
        }


        @Override
        public GpxAttributes getAttributes() {
            return attributes;
        }
    }


}
