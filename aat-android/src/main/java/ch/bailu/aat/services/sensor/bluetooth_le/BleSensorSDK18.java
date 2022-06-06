package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.List;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.SensorInterface;
import ch.bailu.aat.services.sensor.list.SensorItemState;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat.services.sensor.list.SensorListItem;
import ch.bailu.aat.util.AndroidTimer;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.logger.AppLog;

@RequiresApi(api = 18)
public final class BleSensorSDK18 extends BluetoothGattCallback implements SensorInterface {


    private final Executer execute = new Executer();

    private final ServiceInterface[] services;
    private final BluetoothDevice device;

    private final SensorList sensorList;
    private final SensorListItem item;

    private final Context context;
    private final BluetoothGatt gatt;

    private boolean closed = false;
    private int closeState;


    private final AndroidTimer scanningTimeout = new AndroidTimer();

    public BleSensorSDK18(ServiceContext c, BluetoothDevice d, SensorList l, SensorListItem i) {
        synchronized (this) {
            sensorList = l;
            item = i;
            device = d;
            context = c.getContext();

            services = new ServiceInterface[]{
                    new CyclingPower(c),
                    new CscService(c),
                    new HeartRateService(context),
                    new BatteryService(),
            };

            closeState = item.getState();

            gatt = connect();

            if (gatt == null) {
                close();

            } else {
                execute.next(gatt);
                scanningTimeout.kick(() -> {
                    if (item.isScanning())  close();
                }, BleSensorsSDK18.SCAN_DURATION);
                item.setState(SensorItemState.CONNECTING);
                item.setState(SensorItemState.SCANNING);
            }
        }
    }

    private BluetoothGatt connect() {
        try {
            if (item.lock(this)) {
                if (Build.VERSION.SDK_INT >= 23) {
                    return device.connectGatt(context, true, this, BluetoothDevice.TRANSPORT_LE);
                } else {
                    return device.connectGatt(context, true, this);
                }
            }
        } catch (SecurityException e) {
            AppLog.e(this, e);
            return null;
        }
        return null;
    }

    private void updateListItemName() {
        if (item.isScanning() || item.isConnected())
            item.setName(getName());
    }

    @Override
    public synchronized void onConnectionStateChange(BluetoothGatt g, int status, int state) {
        if (isConnected(status, state)) {
            execute.next(gatt);

        } else if (!isConnecting(status, state)) {
            close();
        }
    }

    private void log(int status, int state) {
        AppLog.d(this, "status: " + Integer.toHexString(status) + " state: " + Integer.toHexString(state));
    }


    private static boolean isConnected(int status, int state) {
        return (status == BluetoothGatt.GATT_SUCCESS && state == BluetoothProfile.STATE_CONNECTED);
    }

    private static boolean isConnecting(int status, int state) {
        return (status == BluetoothGatt.GATT_SUCCESS && state == BluetoothProfile.STATE_CONNECTING);
    }

    private void executeNextAndSetState(BluetoothGatt gatt) {
        if (execute.haveToRead()) {
            execute.next(gatt);

        } else {
            setNextState();

            if (item.isConnected()) {
                execute.next(gatt);
            }

        }
    }

    private void setNextState() {
        if (item.isScanning()) {
            close(SensorItemState.SUPPORTED);

        } else if (item.isConnecting()) {
            item.setState(SensorItemState.CONNECTED);
            updateListItemName();
            sensorList.broadcast();

        }
    }

    @Override
    public synchronized void onServicesDiscovered(BluetoothGatt gatt, int status) {

        if (discover(gatt)) {
            executeNextAndSetState(gatt);

        } else {
            close(SensorItemState.UNSUPPORTED);

        }
    }

    private boolean discover(BluetoothGatt gatt) {
        boolean discovered = false;

        List<BluetoothGattService> lists = gatt.getServices();

        for (BluetoothGattService service: lists) {

            List<BluetoothGattCharacteristic> listc = service.getCharacteristics();

            for (ServiceInterface s : services) {                  // scan each sensor to find valid caracteristics
                for (BluetoothGattCharacteristic c : listc) {      // characteristics belong to service
                    if ( s.discovered(c, execute) )
                        discovered =  true;                        // found at least one valid new characteristics
                }
            }
        }
        return discovered;
    }

    @Override
    public synchronized void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor d, int s) {
        executeNextAndSetState(gatt);
    }

    @Override
    public synchronized void onCharacteristicChanged(BluetoothGatt gatt,
                                                     BluetoothGattCharacteristic c) {

        for (ServiceInterface s : services)
            s.changed(c);

    }

    @Override
    public synchronized void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic c,
                                                  int status) {

        for (ServiceInterface s: services)
            s.read(c);

        executeNextAndSetState(gatt);
    }

    @NonNull
    @Override
    public String toString() {
        return device.getName()
                + "@"
                + device.getAddress()
                + ":"
                + item.getSensorStateDescription(context);
    }

    @Override
    public String getName() {
        final StringBuilder builder = new StringBuilder(20);

        if (device.getName() != null)
            builder.append(device.getName());

        for (ServiceInterface s : services) {
            if (s.isValid()) builder.append(" ").append(s);
        }

        return builder.toString();
    }

    public synchronized GpxInformation getInformation(int iid) {
        for (ServiceInterface s : services) {
            if (s.isValid()) {
                GpxInformation i = s.getInformation(iid);
                if (i != null) return i;
            }
        }

        return null;
    }

    private void close(int state) {
        closeState = state;
        close();
    }

    @Override
    public synchronized void close() {
        if (!closed) {
            closed = true;

            scanningTimeout.cancel();

            updateListItemName();

            for (ServiceInterface s : services) s.close();

            if (gatt != null) {
                gatt.disconnect();
                gatt.close();
            }

            if (item.unlock(this)) {

                item.setState(closeState);
                sensorList.broadcast();
            }
        }
    }
}
