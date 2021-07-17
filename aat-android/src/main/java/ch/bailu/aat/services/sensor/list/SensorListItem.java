package ch.bailu.aat.services.sensor.list;

import android.content.Context;

import androidx.annotation.NonNull;

import ch.bailu.aat.R;
import ch.bailu.aat.services.sensor.SensorInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;

public final class SensorListItem extends SensorItemState implements SensorInterface {

    private final static String BLUETOOTH_ADDRESS = "^([0-9A-F]{2}[:]){5}([0-9A-F]{2})$";

    private final String address;
    private String name = "";

    private SensorInterface sensor;

    private final Context context;

    public SensorListItem(Context context, String address, String name, int initialState) {
        super(initialState);
        this.context = context;
        this.address = address;
        setName(name);
    }

    public boolean isBluetoothDevice() {
        return address.matches(BLUETOOTH_ADDRESS);
    }

    public boolean lock(SensorInterface s) {
        if (s == null) {
            return false;

        } else if (!isLocked() || sensor == s) {
            sensor = s;
            return true;

        } else {
            return false;

        }
    }


    public boolean unlock(SensorInterface s) {
        if (isLocked(s)) {
            sensor = null;
            return true;
        }
        return false;
    }


    private boolean isLocked() {
        return sensor != null;
    }

    public boolean isLocked(SensorInterface s) {
        return sensor == s;
    }


    public void setName(String n) {
        if (n != null) name = n;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public GpxInformation getInformation(int iid) {
        if (isLocked()) return sensor.getInformation(iid);
        return null;
    }



    public String getAddress() {
        return address;
    }


    @NonNull
    @Override
    public String toString() {

        final String sensorType = getSensorTypeDescription();
        final String sensorState = getSensorStateDescription(context);
        final String sensorName = getName();


        return sensorType + " " + sensorName + "\n" + sensorState;
    }



    private String getSensorTypeDescription() {
        if (isBluetoothDevice()) {
            return context.getString(R.string.sensor_type_bluetooth);
        } else {
            return context.getString(R.string.sensor_type_internal);
        }
    }



    @Override
    public void close() {
        if (isLocked()) sensor.close();
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            setState(ENABLED);

        } else {
            if (isLocked()) {
                sensor.close();
            }
            setState(SUPPORTED);
        }
    }
}
