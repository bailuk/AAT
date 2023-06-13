package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import androidx.annotation.NonNull;

import ch.bailu.aat.R;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.Sensors;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat.services.sensor.list.SensorListItem;
import ch.bailu.aat.util.AndroidTimer;

public final class BleSensors extends Sensors {

    public final static long SCAN_DURATION = 10 * 1000;

    private final ServiceContext scontext;

    private final BluetoothAdapter adapter;

    private final SensorList sensorList;

    private final AbsBleScanner scannerBle, scannerBonded;

    private boolean scanning = false;

    private final AndroidTimer timer = new AndroidTimer();


    public BleSensors(ServiceContext sc, SensorList list) {
        sensorList = list;
        scontext = sc;

        adapter = getAdapter(sc.getContext());
        scannerBonded = new BleScannerBonded(this);
        scannerBle = AbsBleScanner.factory(this);
    }



    @Override
    public synchronized void scan() throws SecurityException {
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

        if (device != null) {
            new BleSensor(scontext, device, sensorList, item);
        }
    }


    private BluetoothAdapter getAdapter(Context context) {
        BluetoothManager bm = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        if (bm != null)
            return bm.getAdapter();
        return null;
    }

    public BluetoothAdapter getAdapter() {
        return adapter;
    }

    private boolean isEnabled() {
        return adapter != null && adapter.isEnabled();
    }



    public synchronized void foundDevice(BluetoothDevice device) {
        SensorListItem item = sensorList.add(device.getAddress(), device.getName());
        if (item.isUnscanned_or_scanning()) {
            new BleSensor(scontext, device, sensorList, item);
        }
    }



    @NonNull
    @Override
    public  synchronized String toString() {
        if (isEnabled()) {
            if (scanning) {
                return getString(R.string.sensor_bl_scanning);
            } else {
                return getString(R.string.sensor_bl_enabled);
            }
        } else {
            return getString(R.string.sensor_bl_disabled);
        }
    }

    private String getString(int r) {
        return scontext.getContext().getString(r);
    }

    @Override
    public synchronized void close() {
        stopScanner();
    }


    private void startScanner() throws SecurityException {
        scannerBonded.start();
        timer.kick(SCAN_DURATION, this::stopScanner);
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
