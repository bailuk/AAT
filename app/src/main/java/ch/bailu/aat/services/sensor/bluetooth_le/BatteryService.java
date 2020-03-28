package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;
import androidx.annotation.RequiresApi;

import java.util.UUID;

@RequiresApi(api = 18)
public final class BatteryService {

    public final static UUID BATTERY_SERVICE = ID.toUUID(0x180f);
    public final static UUID BATTERY_LEVEL = ID.toUUID(0x2A19);

    private int level = 0;

    public int getBatteryLevelPercentage() {
        return level;
    }


    public boolean discovered(BluetoothGattCharacteristic c, Executer execute) {
        boolean disc = false;
        if (BATTERY_SERVICE.equals(c.getService().getUuid()) && BATTERY_LEVEL.equals(c.getUuid())) {
            disc = true;
            execute.read(c);
        }
        return disc;
    }

    public void read(BluetoothGattCharacteristic c) {
        if (BATTERY_SERVICE.equals(c.getService().getUuid()) && BATTERY_LEVEL.equals(c.getUuid())) {
            logBatteryLevel(c.getValue());
        }
    }

    private void logBatteryLevel(byte[] value) {
        level = value[0];
    }

}
