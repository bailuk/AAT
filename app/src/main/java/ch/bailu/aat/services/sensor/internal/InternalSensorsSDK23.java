package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.RequiresApi;

import java.util.List;

import ch.bailu.aat.services.sensor.Sensors;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat.services.sensor.list.SensorListItem;
import ch.bailu.aat.services.sensor.list.SensorItemState;


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
        //scann(Sensor.TYPE_ALL);
        scann(Sensor.TYPE_PRESSURE);
        scann(Sensor.TYPE_HEART_RATE);
    }


    private void scann(int type) {
        if (manager instanceof SensorManager) {
            List<Sensor> sensors = manager.getSensorList(type);

            if (sensors != null) {
                for (Sensor sensor : sensors) {
                    SensorListItem item =
                            sensorList.add(toAddress(sensor), toName(sensor));

                    if (item.getState() == SensorItemState.UNSCANNED) {
                        item.setState(SensorItemState.SCANNING);
                        if (isSupported(sensor)) {
                            item.setState(SensorItemState.SUPPORTED);
                        } else {
                            item.setState(SensorItemState.UNSUPPORTED);
                        }
                    }
                }
            }
        }

    }

    private boolean isSupported(Sensor sensor) {
        return     sensor.getType() == Sensor.TYPE_PRESSURE
                || sensor.getType() == Sensor.TYPE_HEART_RATE;

    }


    public static String toName(Sensor sensor) {
        return sensor.getVendor() + " " + sensor.getName();
    }

    @Override
    public void updateConnections() {
        for (SensorListItem item : sensorList) {
            if (item.isEnabled() && item.isConnected() == false) {
               factory(item.getAddress(), sensorList);

            }
        }
    }


    private InternalSensorSDK23 factory(String address, SensorList list) {
        if (manager instanceof SensorManager) {
            List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ALL);

            if (sensors != null) {
                for (Sensor sensor : sensors) {

                    if (address.equals(toAddress(sensor))) {
                        return factory(sensor, list);
                    }
                }
            }
        }
        return null;
    }


    private InternalSensorSDK23 factory(Sensor sensor, SensorList list) {
        if (sensor.getType() == Sensor.TYPE_HEART_RATE)
            return new HeartRateSensor(context, list, sensor);
        else if (sensor.getType() == Sensor.TYPE_PRESSURE)
            return new BarometerSensor(context, list, sensor);

        return null;
    }


    public static String toAddress(Sensor sensor) {
        return sensor.getVendor()+sensor.getName();
    }

}
