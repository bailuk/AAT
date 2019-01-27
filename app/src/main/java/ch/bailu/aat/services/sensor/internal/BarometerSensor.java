package ch.bailu.aat.services.sensor.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.support.annotation.RequiresApi;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.preferences.general.SolidUnit;
import ch.bailu.aat.preferences.location.SolidPressureAtSeaLevel;
import ch.bailu.aat.preferences.location.SolidProvideAltitude;
import ch.bailu.aat.services.location.Hypsometric;
import ch.bailu.aat.services.sensor.attributes.IndexedAttributes;
import ch.bailu.aat.util.AppBroadcaster;

@RequiresApi(api = 23)
public class BarometerSensor extends InternalSensorSDK23
        implements SharedPreferences.OnSharedPreferenceChangeListener {


    private final Hypsometric hypsometric = new Hypsometric();

    private final SolidPressureAtSeaLevel spressure;
    private final SolidProvideAltitude saltitude;

    private final Context context;

    private boolean closed = false;

    private GpxInformation information = null;

    public BarometerSensor(Context c, Sensor sensor) {
        super(c, sensor, InfoID.BAROMETER_SENSOR);

        context = c;
        spressure  = new SolidPressureAtSeaLevel(c);
        saltitude  = new SolidProvideAltitude(c, SolidUnit.SI);

        hypsometric.setPressureAtSeaLevel(spressure.getPressure());

        spressure.register(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float pressure = getPressure(event);
        hypsometric.setPressure(pressure);

        information = new Information(hypsometric.getAltitude(), pressure);


        AppBroadcaster.broadcast(context,
                 AppBroadcaster.SENSOR_CHANGED + InfoID.BAROMETER_SENSOR);


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
    public void onSharedPreferenceChanged(SharedPreferences p, String key) {
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
        if (closed == false) {
            closed = true;
            spressure.unregister(this);
        }
    }


    public static class Information extends GpxInformation {

        private final GpxAttributes attributes;
        private final short altitude;
        private long time = System.currentTimeMillis();

        public Information(short a, float p) {
            attributes = new Attributes(p);
            altitude = a;
        }


        @Override
        public GpxAttributes getAttributes() {
            return attributes;
        }


        @Override
        public short getAltitude() {
            return altitude;
        }


        @Override
        public long getTimeStamp() {
            return time;
        }
    }


    public static class Attributes extends IndexedAttributes {

        final float pressure;

        public static final float KEY_INDEX_PRESSURE=0;

        public static final String[] KEYS = {
                "Pressure"
        };



        public Attributes(float p) {
            super(KEYS);
            pressure = p;
        }

        @Override
        public String getValue(int index) {
            if (index == KEY_INDEX_PRESSURE) return String.valueOf(pressure);
            return "";
        }
    }
}
