package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.support.annotation.RequiresApi;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.Sensors;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat.services.sensor.list.SensorListItem;
import ch.bailu.aat.util.Timer;
import ch.bailu.aat.util.ToDo;

@RequiresApi(api = 18)
public class BleSensorsSDK18 extends Sensors {

    public final static long SCAN_DURATION = 10 * 1000;
    public static final long CONNECTING_DURATION = 60 * 1000;

    private final Context context;
    private final ServiceContext scontext;

    private final BluetoothAdapter adapter;

    private final SensorList sensorList;

    private final BleScanner scannerBle, scannerBonded;

    private boolean scanning = false;


    private final Timer timer = new Timer(new Runnable() {
        @Override
        public void run() {
            stopScanner();
        }
    }, SCAN_DURATION);




    public BleSensorsSDK18(ServiceContext sc, SensorList list) {
        sensorList = list;
        scontext = sc;
        context = sc.getContext();

        adapter = getAdapter(context);
        scannerBonded = new BleScannerBonded(this);
        scannerBle = BleScanner.factory(this);
    }



    @Override
    public  synchronized void scann() {
        stopScanner();

        if (isEnabled()) {
            startScanner();
        }
    }


    @Override
    public synchronized void updateConnections() {
        for (SensorListItem item : sensorList) {
            if (item.isBluetoothDevice()) {
                if (isEnabled()) {
                    if (item.isEnabled() && !item.isOpen()) {
                        connect(item);
                    }
                } else {
                    item.close();
                }
            }
        }
    }



    private void connect(SensorListItem item) {
        final BluetoothDevice device = adapter.getRemoteDevice(item.getAddress());

        if (device instanceof  BluetoothDevice) {
            new BleSensorSDK18(scontext, device, sensorList);
        }
    }


    private BluetoothAdapter getAdapter(Context context) {
        BluetoothManager bm = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        if (bm instanceof  BluetoothManager)
            return bm.getAdapter();
        return null;
    }

    public BluetoothAdapter getAdapter() {
        return adapter;
    }

    private boolean isEnabled() {
        return adapter instanceof  BluetoothAdapter && adapter.isEnabled();
    }





    public synchronized void foundDevice(BluetoothDevice device) {
        if (sensorList.add(device.getAddress(), device.getName()).isUnscanned()) {
            new BleSensorSDK18(scontext, device, sensorList);
        }
    }



    @Override
    public  synchronized String toString() {
        if (isEnabled()) {
            if (scanning)
                return ToDo.translate("Scanning for Bluetooth sensors...");

            return ToDo.translate("Bluetooth is enabled");

        } else {
            return ToDo.translate("Bluetooth is disabled");
        }
    }


    @Override
    public synchronized void close() {
        stopScanner();
    }


    private void startScanner() {
        scannerBonded.start();
        timer.kick();
        scannerBle.start();

        scanning = isEnabled();
        sensorList.broadcast();
    }


    private void stopScanner() {
        scanning = false;
        timer.cancel();
        scannerBle.stop();
        sensorList.broadcast();
    }
}
