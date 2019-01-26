package ch.bailu.aat.services.sensor.list;

import android.content.Context;

import java.io.Closeable;
import java.util.ArrayList;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.StateID;
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
        SensorListItem item = find(address);

        if (item == null) {
            item = new SensorListItem(address, name);
            add(item);

        } else {
            item.setName(name);
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
                if (i.isConnectionEstablished()) {
                    sensorCount++;
                } else if (i.isConnected()) {
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

        public static final int KEY_SENSOR_COUNT=0;

        private final int sensors;


        public Attributes(int s) {
            sensors = s;
        }


        public static final String[] KEYS = {
            "SensorCount"
        };

        @Override
        public String get(String key) {
            return String.valueOf(sensors);
        }

        @Override
        public String getValue(int index) {
            return String.valueOf(sensors);
        }

        @Override
        public String getKey(int index) {
            return KEYS[KEY_SENSOR_COUNT];
        }

        @Override
        public void put(String key, String value) {

        }

        @Override
        public int size() {
            return KEYS.length;
        }

        @Override
        public void remove(String key) {

        }
    }
}
