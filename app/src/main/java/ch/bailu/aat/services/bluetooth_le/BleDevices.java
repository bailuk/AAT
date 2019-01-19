package ch.bailu.aat.services.bluetooth_le;

import android.content.Context;
import android.os.Build;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxInformation;

public class BleDevices implements Closeable {

    public void scann() {}
    public GpxInformation getInformation() {
        return GpxInformation.NULL;
    }


    public static BleDevices factory(Context context) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new BleDevicesSDK18(context);
        } else {
            return new BleDevices();
        }
    }

    @Override
    public void close() {}
}
