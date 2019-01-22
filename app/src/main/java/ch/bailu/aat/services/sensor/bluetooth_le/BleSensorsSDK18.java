package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.support.annotation.RequiresApi;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.Sensors;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.Timer;
import ch.bailu.aat.util.ToDo;

@RequiresApi(api = 18)
public class BleSensorsSDK18 extends Sensors {

    private final static long SCAN_DURATION = 5000;

    private final Context context;
    private final ServiceContext scontext;

    private final BluetoothAdapter adapter;

    private final Devices devices = new Devices();
    private final BleScanner scanner;
    private final Timer timer = new Timer(new Runnable() {
        @Override
        public void run() {
            scanner.stop();
        }
    }, SCAN_DURATION);




    public BleSensorsSDK18(ServiceContext sc) {
        scontext = sc;
        context = sc.getContext();

        adapter = getAdapter();
        scanner = BleScanner.factory(adapter, this);

        scann();
    }



    @Override
    public  synchronized void scann() {
        stopScanner();

        if (isEnabled()) {
            devices.closeDisconnectedDevices();
            startScanner();

        } else {
            devices.closeAllDevices();
        }

        AppBroadcaster.broadcast(context, AppBroadcaster.BLE_DEVICE_SCANNED);
    }


    private BluetoothAdapter getAdapter() {
        BluetoothManager bm = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        return bm.getAdapter();
    }


    private boolean isEnabled() {
        return adapter instanceof  BluetoothAdapter && adapter.isEnabled();
    }





    public synchronized void foundDevice(BluetoothDevice device) {
        if (!devices.isInList(device)) {
            devices.addAndConnectDevice(scontext, device);
        }
    }



    @Override
    public  synchronized String toString() {
        if (isEnabled()) {
            return devices.toString();

        } else {
            return ToDo.translate("Bluetooth is disabled") + "\n";
        }
    }


    @Override
    public synchronized GpxInformation getInformation(int iid) {

        GpxInformation information = devices.getInformation(iid);

        if (information == null) {
            information = GpxInformation.NULL;
        }

        return information;
    }


    @Override
    public synchronized void close() {
        stopScanner();
        devices.close();
    }


    private void startScanner() {
        timer.kick();
        scanner.start();
    }


    private void stopScanner() {
        timer.cancel();
        scanner.stop();
    }
}
