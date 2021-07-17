package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.UUID;

import ch.bailu.aat_lib.gpx.GpxInformation;

@RequiresApi(api = 18)
public final class BatteryService implements ServiceInterface {

    public final static UUID BATTERY_SERVICE = ID.toUUID(0x180f);
    public final static UUID BATTERY_LEVEL = ID.toUUID(0x2A19);

    private boolean valid = false;

    private int level = 0;

    public int getBatteryLevelPercentage() {
        return level;
    }

    @Override
    public void close() {
        // TODO
    }

    @Override
    public void changed(BluetoothGattCharacteristic c) {
        // TODO
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean discovered(BluetoothGattCharacteristic c, Executer execute) {
        boolean disc = false;
        if (BATTERY_SERVICE.equals(c.getService().getUuid()) && BATTERY_LEVEL.equals(c.getUuid())) {
            valid = true;
            disc = true;
            execute.read(c);
        }
        return disc;
    }

    @Override
    public void read(BluetoothGattCharacteristic c) {
        if (BATTERY_SERVICE.equals(c.getService().getUuid()) && BATTERY_LEVEL.equals(c.getUuid())) {
            logBatteryLevel(c.getValue());
        }
    }

    private void logBatteryLevel(byte[] value) {
        level = value[0];
    }

    @Override
    public GpxInformation getInformation(int iid) {
        // TODO
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Battery=" + level + "%";
    }
}
