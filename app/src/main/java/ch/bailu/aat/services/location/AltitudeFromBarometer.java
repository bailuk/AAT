package ch.bailu.aat.services.location;

/*
public class AltitudeFromBarometer extends LocationStackChainedItem implements SensorEventListener {
    private final Hypsometric hypsometric = new Hypsometric();
    private final Barometer barometer;

    private final SolidAltitudeFromBarometer sbarometer;
    private final SolidPressureAtSeaLevel    spressure;
    private final SolidProvideAltitude       saltitude;


    private boolean enabled;


    public AltitudeFromBarometer(LocationStackItem n, Context c) {
        super(n);
        barometer = new Barometer(c);
        barometer.requestUpdates(this);

        sbarometer = new SolidAltitudeFromBarometer(c);
        spressure  = new SolidPressureAtSeaLevel(c);
        saltitude  = new SolidProvideAltitude(c, SolidUnit.SI);

        enabled    = sbarometer.isEnabled() && barometer.haveSensor();

        hypsometric.setPressureAtSeaLevel(spressure.getPressure());
    }


    @Override
    public void preferencesChanged(Context c, String key, int presetIndex) {
        if (saltitude.hasKey(key)) {
            hypsometric.setAltitude(saltitude.getValue());
            if (hypsometric.isPressureAtSeaLevelValid()) {
                spressure.setPressure((float) hypsometric.getPressureAtSeaLevel());
            }

        } else if (spressure.hasKey(key)) {
            hypsometric.setPressureAtSeaLevel(spressure.getPressure());

        } else if (sbarometer.hasKey(key)) {
            enabled = sbarometer.isEnabled();
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        hypsometric.setPressure(Barometer.getPressure(event));
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void close() {
        barometer.cancelUpdates(this);
    }



    @Override
    public void passLocation(LocationInformation l) {


        if (enabled && hypsometric.isValid()) {
            l.setAltitude(hypsometric.getAltitude());
        }

        super.passLocation(l);
    }
}
*/