package ch.bailu.aat.services.sensor;

import android.content.Context;
import android.os.Build;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.bluetooth_le.BleSensorsSDK18;
import ch.bailu.aat.services.sensor.internal.InternalSensorsSDK23;
import ch.bailu.aat.services.sensor.list.SensorList;

public class Sensors implements Closeable {

    public void updateConnections() {}
    public void scann() {}

    public GpxInformation getInformation(int iid) {
        return GpxInformation.NULL;
    }


    public static Sensors factoryBle(ServiceContext sc, SensorList sensorList) {
        if (Build.VERSION.SDK_INT >= 18) {
            return new BleSensorsSDK18(sc, sensorList);
        } else {
            return new Sensors();
        }
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
