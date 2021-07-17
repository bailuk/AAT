package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothGattCharacteristic;

import ch.bailu.aat_lib.gpx.GpxInformation;

public interface ServiceInterface {
    void close();

    void changed(BluetoothGattCharacteristic c);

    boolean isValid();

    boolean discovered(BluetoothGattCharacteristic c, Executer execute);

    void read(BluetoothGattCharacteristic c);

    GpxInformation getInformation(int iid);
}
