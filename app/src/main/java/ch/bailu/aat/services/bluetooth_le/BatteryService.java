package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.RequiresApi;

import java.util.UUID;

@RequiresApi(api = 18)
public class BatteryService {

    public final static UUID BATTERY_SERVICE = ID.toUUID(0x180f);
    public final static UUID BATTERY_LEVEL = ID.toUUID(0x2A19);

    private int level = 0;

    public int getBatteryLevelPercentage() {
        return level;
    }


    public void discovered(BluetoothGattCharacteristic c, Executer execute) {
        if (BATTERY_SERVICE.equals(c.getService().getUuid()) && BATTERY_LEVEL.equals(c.getUuid())) {
            execute.read(c);
        }
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
