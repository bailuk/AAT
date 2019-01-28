package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.RequiresApi;

import java.util.List;

import ch.bailu.aat.services.sensor.Sensors;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat.services.sensor.list.SensorListItem;
import ch.bailu.aat.services.sensor.list.SensorStateID;


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
            List<Sensor> sensors = manager.getSensorList(type);

            if (sensors != null) {
                for (Sensor sensor : sensors) {
                    SensorListItem item =
                            sensorList.add(toAddress(sensor), sensor.getVendor() + " " + sensor.getName());

                    if (!item.isEnabled()) {
                        item.setState(SensorStateID.SCANNING);
                        item.setState(SensorStateID.VALID);
                    }

                }
            }
        }

    }

    @Override
    public void updateConnections() {
        for (SensorListItem item : sensorList) {
            if (item.isEnabled() && item.isConnected() == false) {

               createSensorFromAddress(item.getAddress(), sensorList);
            }
        }
    }


    private InternalSensorSDK23 createSensorFromAddress(String address, SensorList list) {
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
