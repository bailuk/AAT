package ch.bailu.aat.services.sensor.list;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.sensor.SensorInterface;
import ch.bailu.aat.util.ToDo;

public class SensorListItem extends SensorItemState implements SensorInterface {

    private final static String BLUETOOTH_ADDRESS = "^([0-9A-F]{2}[:]){5}([0-9A-F]{2})$";

    private final String address;
    private String name = "";

    private SensorInterface sensor;



    public SensorListItem(String a, String n, int initialState) {
        super(initialState);

        address = a;
        setName(n);
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


    @Override
    public String toString() {

        final String sensorType = getSensorTypeDescription();
        final String sensorState = getSensorStateDescription();
        final String sensorName = getName();


        return sensorType + " " + sensorName + "\n" + sensorState;
    }



    public String getSensorTypeDescription() {
        if (isBluetoothDevice()) {
            return "Bluetooth";
        } else {
            return ToDo.translate("Internal");
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
