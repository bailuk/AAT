package ch.bailu.aat.gpx;

import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.services.sensor.attributes.IndexedAttributes;

public class GpxTrackAttributes extends IndexedAttributes {

    public static final GpxTrackAttributes NULL = factoryNull();

    private static final String[] KEYS = {
            "MaxSpeed",
            "AutoPause",
            "Slope",
            "Descend",
            "Ascend",
            "AverageCadence",
            "TotalCadence",
            "AverageHR",
            "HeartBeats",
    };

    public static final int INDEX_MAX_SPEED = 0;
    public static final int INDEX_AUTO_PAUSE = 1;
    public static final int INDEX_SLOPE = 2;
    public static final int INDEX_DESCEND = 3;
    public static final int INDEX_ASCEND = 4;
    private static final int INDEX_AVERAGE_CADENCE = 5;
    private static final int INDEX_TOTAL_CADENCE = 6;
    private static final int AVERAGE_HEART_RATE = 7;
    private static final int TOTAL_HEART_BEATS = 8;

    private final MaxSpeed maximumSpeed;
    private final AutoPause autoPause;
    private final AltitudeDelta altitudeDelta;



    public GpxTrackAttributes(MaxSpeed max, AutoPause pause, AltitudeDelta altitude) {
        super(KEYS);
        maximumSpeed = max;
        autoPause = pause;
        altitudeDelta = altitude;
    }


    public static GpxTrackAttributes factoryNull() {
        return new GpxTrackAttributes(MaxSpeed.NULL, AutoPause.NULL, AltitudeDelta.NULL);
    }


    public static GpxTrackAttributes factorySummary() {
        return new GpxTrackAttributes(new MaxSpeed.Raw2(), AutoPause.NULL, AltitudeDelta.NULL);
    }

    public static GpxTrackAttributes factoryTrackLogger(SolidAutopause spause) {
        return new GpxTrackAttributes(new MaxSpeed.Samples(),
                new AutoPause.Time(
                        spause.getTriggerSpeed(),
                        spause.getTriggerLevelMillis()),
                new AltitudeDelta.LastAverage());


    }


    @Override
    public String getValue(int index) {
        if (index == INDEX_MAX_SPEED) {
            return String.valueOf(maximumSpeed.get());
        } else if (index == INDEX_AUTO_PAUSE) {
            return String.valueOf(autoPause.get());
        } else if (index == INDEX_SLOPE) {
            return String.valueOf(altitudeDelta.getSlope());
        } else if (index == INDEX_DESCEND) {
            return String.valueOf(altitudeDelta.getDescend());
        } else if (index == INDEX_ASCEND) {
            return String.valueOf(altitudeDelta.getAscend());
        }

        return "";
    }


    @Override
    public float getFloatValue(int index) {
        if (index == INDEX_MAX_SPEED)
            return maximumSpeed.get();
        else if (index == INDEX_ASCEND)
            return altitudeDelta.getAscend();
        else if (index == INDEX_DESCEND)
            return altitudeDelta.getDescend();

        return super.getFloatValue(index);
    }


    @Override
    public long getLongValue(int index) {
        if (index == INDEX_AUTO_PAUSE) {
            return autoPause.get();
        }
        return super.getLongValue(index);
    }


        public void update(GpxPointNode p) {
        maximumSpeed.add(p.getSpeed());

        if (autoPause.update(p)) {
            altitudeDelta.add(p.getAltitude(), p.getDistance());
        }
    }


}
