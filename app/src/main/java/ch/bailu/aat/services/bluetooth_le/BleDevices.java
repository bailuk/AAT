package ch.bailu.aat.services.bluetooth_le;

import android.content.Context;
import android.os.Build;

public class BleDevices {

    public void scann() {}


    public static BleDevices factory(Context context) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new BleDevicesSDK18(context);
        } else {
            return new BleDevices();
        }
    }
}
