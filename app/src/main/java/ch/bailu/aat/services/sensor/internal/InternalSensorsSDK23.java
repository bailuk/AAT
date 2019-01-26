package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.RequiresApi;

import java.util.List;

import ch.bailu.aat.services.sensor.Sensors;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat.services.sensor.list.SensorListItem;


@RequiresApi(api = 23)
public class InternalSensorsSDK23 extends Sensors {

    private final SensorManager manager;
    private final Context context;

    private final SensorList sensorList;

    public InternalSensorsSDK23(Context c, SensorList list) {
        sensorList = list;
        context = c;
        manager = context.getSystemService(SensorManager.class);

        scann();
    }


    @Override
    public void scann() {
        scann(Sensor.TYPE_HEART_RATE);
        scann(Sensor.TYPE_PRESSURE);
    }


    private void scann(int type) {
        if (manager instanceof SensorManager) {
            List<Sensor> heartRateSensors = manager.getSensorList(type);

            if (heartRateSensors != null) {
                for (Sensor sensor : heartRateSensors) {
                    sensorList.add(toAddress(sensor), sensor.getVendor() + " " + sensor.getName());
                }
            }
        }

    }

    @Override
    public void updateConnections() {
        for (SensorListItem item : sensorList) {
            if (item.isEnabled() && item.isConnected() == false) {
                final InternalSensorSDK23 sensor = createSensorFromAddress(item.getAddress());
                if (sensor != null) {
                    item.setSensor(sensor);
                }
            }
        }
    }


    private InternalSensorSDK23 createSensorFromAddress(String address) {
        if (manager instanceof SensorManager) {
            List<Sensor> heartRateSensors = manager.getSensorList(Sensor.TYPE_HEART_RATE);

            if (heartRateSensors != null) {
                for (Sensor sensor : heartRateSensors) {

                    if (address.equals(toAddress(sensor))) {
                        return new HeartRateSensor(context, sensor);
                    }
                }
            }
        }
        return null;
    }


    public static String toAddress(Sensor sensor) {
        return sensor.getVendor()+sensor.getName();
    }

}
