package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import androidx.annotation.RequiresApi;

import ch.bailu.aat.preferences.location.SolidPressureAtSeaLevel;
import ch.bailu.aat.preferences.location.SolidProvideAltitude;
import ch.bailu.aat.services.location.Hypsometric;
import ch.bailu.aat.services.sensor.list.SensorListItem;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.attributes.Keys;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidUnit;

@RequiresApi(api = 23)
public final class BarometerSensor extends InternalSensorSDK23
        implements OnPreferencesChanged {

    private static final String changedAction =
        AppBroadcaster.SENSOR_CHANGED + InfoID.BAROMETER_SENSOR;

    private final Hypsometric hypsometric = new Hypsometric();

    private final SolidPressureAtSeaLevel spressure;
    private final SolidProvideAltitude saltitude;

    private final Context context;


    private GpxInformation information = null;

    public BarometerSensor(Context c, SensorListItem item, Sensor sensor) {
        super(c, item, sensor, InfoID.BAROMETER_SENSOR);

        context = c;
        spressure  = new SolidPressureAtSeaLevel(c);
        saltitude  = new SolidProvideAltitude(c, SolidUnit.SI);

        if (isLocked()) {
            hypsometric.setPressureAtSeaLevel(spressure.getPressure());

            spressure.register(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float pressure = getPressure(event);
        hypsometric.setPressure(pressure);

        information = new Information(hypsometric.getAltitude(), pressure);


        OldAppBroadcaster.broadcast(context, changedAction);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    public static float getPressure(SensorEvent event) {
        if (event.values.length > 0) {
            return event.values[0];
        }
        return 0f;
    }



    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {

        if (saltitude.hasKey(key)) {
            hypsometric.setAltitude(saltitude.getValue());
            if (hypsometric.isPressureAtSeaLevelValid()) {
                spressure.setPressure((float) hypsometric.getPressureAtSeaLevel());
            }

        } else if (spressure.hasKey(key)) {
            hypsometric.setPressureAtSeaLevel(spressure.getPressure());

        }
    }

    @Override
    public GpxInformation getInformation(int iid) {
        if (iid == InfoID.BAROMETER_SENSOR)
            return information;
        return null;
    }


    @Override
    public void close() {
        if (isLocked()) {
            spressure.unregister(this);
        }

        super.close();
    }


    public static class Information extends GpxInformation {

        private final GpxAttributes attributes;
        private final double altitude;
        private final long time = System.currentTimeMillis();

        public Information(double a, float p) {
            attributes = new Attributes(p);
            altitude = a;
        }


        @Override
        public GpxAttributes getAttributes() {
            return attributes;
        }


        @Override
        public double getAltitude() {
            return  altitude;
        }


        @Override
        public long getTimeStamp() {
            return time;
        }
    }


    public static class Attributes extends GpxAttributes {

        final float pressure;

        public static final int KEY_INDEX_PRESSURE= Keys.toIndex("Pressure");




        public Attributes(float p) {
            pressure = p;
        }

        @Override
        public String get(int index) {
            if (index == KEY_INDEX_PRESSURE) return String.valueOf(pressure);
            return NULL_VALUE;
        }

        @Override
        public boolean hasKey(int keyIndex) {
            return keyIndex == KEY_INDEX_PRESSURE;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String getAt(int i) {
            return get(KEY_INDEX_PRESSURE);
        }

        @Override
        public int getKeyAt(int i) {
            return KEY_INDEX_PRESSURE;
        }
    }
}
