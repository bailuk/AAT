package ch.bailu.aat.gpx;

import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.services.sensor.attributes.IndexedAttributes;

public class GpxListAttributes extends IndexedAttributes {

    public static final GpxListAttributes NULL = factoryNull();

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
    public static final int INDEX_CADENCE = 5;
    public static final int INDEX_TOTAL_CADENCE = 6;
    public static final int INDEX_AVERAGE_HR = 7;
    public static final int INDEX_HEART_BEATS = 8;

    private final MaxSpeed maximumSpeed;
    private final AutoPause autoPause;
    private final AltitudeDelta altitudeDelta;
    private final SampleRate cadence;
    private final SampleRate hr;



    public GpxListAttributes(MaxSpeed max,
                             AutoPause pause,
                             AltitudeDelta altitude,
                             SampleRate c,
                             SampleRate h) {
        super(KEYS);
        maximumSpeed = max;
        autoPause = pause;
        altitudeDelta = altitude;
        hr = h;
        cadence = c;
    }


    public static GpxListAttributes factoryNull() {
        return new GpxListAttributes(MaxSpeed.NULL, AutoPause.NULL,
                AltitudeDelta.NULL, SampleRate.NULL, SampleRate.NULL);
    }


    public static GpxListAttributes factoryTrackList() {
        return new GpxListAttributes(new MaxSpeed.Raw2(), AutoPause.NULL, AltitudeDelta.NULL,
                SampleRate.NULL, SampleRate.NULL);
    }


    public static GpxListAttributes factoryTrack(SolidAutopause spause) {
        return factoryTrack(new AutoPause.Time(
                        spause.getTriggerSpeed(),
                        spause.getTriggerLevelMillis()));
    }


    public static GpxListAttributes factoryTrack(AutoPause apause) {
        return new GpxListAttributes(new MaxSpeed.Samples(),
                apause,
                new AltitudeDelta.LastAverage(),
                new SampleRate.Cadence(), new SampleRate.HeartRate());
    }

    public static GpxListAttributes factoryRoute() {
        return new GpxListAttributes(MaxSpeed.NULL, AutoPause.NULL,
                new AltitudeDelta.LastAverage(), SampleRate.NULL, SampleRate.NULL);
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

        } else if (index == INDEX_CADENCE) {
            return String.valueOf(cadence.getAverageSpm());

        } else if (index == INDEX_TOTAL_CADENCE) {
            return String.valueOf(cadence.getTotalSamples());

        } else if (index == INDEX_HEART_BEATS) {
            return String.valueOf(hr.getTotalSamples());

        } else if (index == INDEX_AVERAGE_HR) {
            return String .valueOf(hr.getAverageSpm());
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
        hr.add(p);
        cadence.add(p);

        if (autoPause.update(p)) {
            altitudeDelta.add(p.getAltitude(), p.getDistance());
        }
    }
}
