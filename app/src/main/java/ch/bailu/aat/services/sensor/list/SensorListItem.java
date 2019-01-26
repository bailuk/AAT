package ch.bailu.aat.services.sensor.list;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.sensor.SensorInterface;
import ch.bailu.aat.util.ToDo;

public class SensorListItem implements SensorInterface {
    private String name;
    private final String address;

    private boolean enabled;

    private SensorInterface sensor;



    public SensorListItem(String a, String n) {
        address = a;
        name = n;
    }

    public void setSensor(SensorInterface s) {
        if (s != sensor) disconnect();
        sensor = s;
    }


    public boolean isConnected() {
        return sensor != null;
    }


    public void setEnabled(boolean e) {
        if (e) enable();
        else disable();
    }


    public boolean isEnabled() {
        return enabled;
    }


    public void enable() {
        enabled = true;
    }


    public void disable() {
        enabled = false;
        disconnect();
    }


    public void disconnect() {
        if (isConnected()) {
            sensor.close();
            sensor = null;
        }
    }


    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }


    @Override
    public GpxInformation getInformation(int iid) {
        if (sensor != null) return sensor.getInformation(iid);
        return null;
    }

    @Override
    public boolean isConnectionEstablished() {
        return isConnected() && sensor.isConnectionEstablished();
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        String sensorState = ToDo.translate("Not connected");
        String sensorType = ToDo.translate("Internal");
        String sensorName = getName();

        if (isConnected()) {
            if (isConnectionEstablished()) {
                sensorState = ToDo.translate("Connection established");
            } else if (isConnected()) {
                sensorState = ToDo.translate("Is connecting...");
            }
        }

        if (isBluetoothDevice()) {
            sensorType = "Bluetooth";
        }

        return sensorType + " " + sensorName + "\n" + sensorState;
    }


    private final static String BLUETOOTH_ADDRESS = "^([0-9A-F]{2}[:]){5}([0-9A-F]{2})$";

    public boolean isBluetoothDevice() {
        return address.matches(BLUETOOTH_ADDRESS);
    }


    @Override
    public void close() {
        disconnect();
    }
}
