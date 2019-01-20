package ch.bailu.aat.services.bluetooth_le;

import android.os.Build;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;

public class BleDevices implements Closeable {

    public void scann() {}
    public GpxInformation getInformation(int iid) {
        return GpxInformation.NULL;
    }


    public static BleDevices factory(ServiceContext sc) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new BleDevicesSDK18(sc);
        } else {
            return new BleDevices();
        }
    }

    @Override
    public void close() {}
}
