package ch.bailu.aat.services.sensor.list;

import android.content.Context;

import java.io.Closeable;
import java.util.ArrayList;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.attributes.Keys;
import ch.bailu.aat.services.sensor.SensorInterface;
import ch.bailu.aat.util.AppBroadcaster;

public class SensorList extends ArrayList<SensorListItem> implements Closeable {

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
            item = new SensorListItem(address, name, initialState);
            add(item);

        }

        return item;

    }


    public SensorListItem add(SensorInterface sensor) {
        return add(sensor.getAddress(), sensor.getName());
    }



    public SensorListItem find(String address) {
        for (SensorListItem i : this) {
            if (i.getAddress().equalsIgnoreCase(address)) return i;
        }
        return null;
    }


    public void broadcast() {
        AppBroadcaster.broadcast(context, AppBroadcaster.SENSOR_CHANGED + InfoID.SENSORS);
    }


    public GpxInformation getInformation(int iid) {
        GpxInformation i = null;

        if (iid == InfoID.SENSORS)
            return new Information();

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



    public class Information extends GpxInformation {
        private int state = StateID.OFF;
        private int sensorCount = 0;

        private final Attributes attributes;

        public Information() {
            for (SensorListItem i : SensorList.this) {
                if (i.isConnected()) {
                    sensorCount++;
                } else if (i.isConnecting()) {
                    state = StateID.WAIT;
                }
            }

            if (state != StateID.WAIT && sensorCount > 0)
                state = StateID.ON;

            attributes = new Attributes(sensorCount);
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


    public static class Attributes extends GpxAttributes {

        private static final Keys KEYS = new Keys();
        public static final int KEY_SENSOR_COUNT = KEYS.add("count");
        public static final int KEY_SENSOR_OVERVIEW = KEYS.add("overview");


        private final int sensors;


        public Attributes(int s) {
            sensors = s;
        }



        @Override
        public String get(int key) {
            if (key == KEY_SENSOR_COUNT) {
                return String.valueOf(sensors);
            } else if (key == KEY_SENSOR_OVERVIEW) {
                return SensorState.getOverviewString();
            }
            return NULL_VALUE;
        }

        @Override
        public boolean hasKey(int keyIndex) {
            return KEYS.hasKey(keyIndex);
        }

        @Override
        public int size() {
            return KEYS.size();
        }

        @Override
        public String getAt(int i) {
            return get(getKeyAt(i));
        }

        @Override
        public int getKeyAt(int i) {
            return KEYS.getKeyIndex(i);
        }
    }
}
