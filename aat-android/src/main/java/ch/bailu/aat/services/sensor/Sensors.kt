package ch.bailu.aat.services.sensor;

import android.content.Context;
import android.os.Build;

import java.io.Closeable;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.bluetooth_le.BleSensors;
import ch.bailu.aat.services.sensor.internal.InternalSensorsSDK23;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat_lib.gpx.GpxInformation;

public class Sensors implements Closeable {

    public void updateConnections() {}
    public void scan() {}

    public GpxInformation getInformation() {
        return GpxInformation.NULL;
    }


    public static Sensors factoryBle(ServiceContext sc, SensorList sensorList) {
        return new BleSensors(sc, sensorList);
    }


    public static Sensors factoryInternal(Context c, SensorList sensorList) {
        if (Build.VERSION.SDK_INT >= 23) {
            return new InternalSensorsSDK23(c, sensorList);
        } else {
            return new Sensors();
        }
    }

    @Override
    public void close() {}


}
