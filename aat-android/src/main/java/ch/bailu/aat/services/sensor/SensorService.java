package ch.bailu.aat.services.sensor;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;

import androidx.annotation.NonNull;

import ch.bailu.aat.dispatcher.AndroidBroadcaster;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.service.VirtualService;
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface;
import ch.bailu.aat_lib.util.WithStatusText;

public final class SensorService extends VirtualService implements WithStatusText, SensorServiceInterface {
    private final SensorList sensorList;

    private final Sensors bluetoothLE;
    private final Sensors internal;

    private final Broadcaster broadcaster;

    public SensorService(ServiceContext sc) {
        sensorList = new SensorList(sc.getContext());
        bluetoothLE = Sensors.factoryBle(sc, sensorList);
        internal = Sensors.factoryInternal(sc.getContext(), sensorList);

        broadcaster = new AndroidBroadcaster(sc.getContext());

        broadcaster.register(onBluetoothStateChanged, BluetoothAdapter.ACTION_STATE_CHANGED);
        broadcaster.register(onSensorDisconnected, AppBroadcaster.SENSOR_DISCONNECTED + InfoID.SENSORS);
        broadcaster.register(onSensorReconnect, AppBroadcaster.SENSOR_RECONNECT + InfoID.SENSORS);

        updateConnections();
    }


    public static boolean isSupported() {
        return (Build.VERSION.SDK_INT >= 18);
    }


    final BroadcastReceiver onBluetoothStateChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Object ...args) {
            Integer state = (Integer) args[0];
            if (state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_OFF) {
                updateConnections();
            }
        }


    };


    final BroadcastReceiver onSensorDisconnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Object ...args) {
            updateConnections();
        }
    };


    final BroadcastReceiver onSensorReconnect = new BroadcastReceiver() {
        @Override
        public void onReceive(Object ...args) {
            updateConnections();
            scan();                        // rescan to get them in cache if they were not
        }
    };


    @Override
    public void appendStatusText(StringBuilder builder) {
        builder.append(toString());
    }


    public synchronized void close() {
        bluetoothLE.close();
        internal.close();
        sensorList.close();
        broadcaster.unregister(onBluetoothStateChanged);
        broadcaster.unregister(onSensorDisconnected);
        broadcaster.unregister(onSensorReconnect);
    }

    public synchronized void updateConnections() {
        bluetoothLE.updateConnections();
        internal.updateConnections();
        sensorList.broadcast();
    }

    public synchronized void scan() {
        bluetoothLE.scan();
    }


    @NonNull
    @Override
    public synchronized String toString() {
        return bluetoothLE.toString();

    }

    public synchronized GpxInformation getInformation(int iid) {
        GpxInformation information = getInformationOrNull(iid);


        if (information == null) {
            information = GpxInformation.NULL;
        }

        return information;
    }


    public synchronized GpxInformation getInformationOrNull(int iid) {
        return sensorList.getInformation(iid);
    }


    public SensorList getSensorList() {
        return sensorList;
    }
}