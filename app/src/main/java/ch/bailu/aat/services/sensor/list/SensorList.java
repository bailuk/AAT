package ch.bailu.aat.services.sensor.list;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
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



    private final static String PREFERENCES = "SensorList";
    private final static String KEY_ADDRESS = "Address";
    private final static String KEY_NAME = "Name";



    public GpxInformation getInformation(int iid) {
        GpxInformation i = null;
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
        final SharedPreferences settings = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();

        final Set<String> addresses = new HashSet<>();
        final Set<String> names = new HashSet<>();
        for (SensorListItem i: this) {
            if (i.isEnabled()) {
                addresses.add(i.getAddress());
                names.add(i.getName());
            }
        }

        editor.putStringSet(KEY_ADDRESS, addresses);
        editor.putStringSet(KEY_NAME, names);
        editor.apply();
    }


    private void restore() {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        Set<String> addresses = settings.getStringSet(KEY_ADDRESS, new HashSet<>());
        Set<String> names = settings.getStringSet(KEY_NAME, new HashSet<>());


        if (addresses != null && names != null) {
            Iterator<String> address = addresses.iterator();
            Iterator<String> name = names.iterator();

            while (address.hasNext() && name.hasNext()) {
                add(address.next(), name.next()).enable();
            }
        }
    }
}
