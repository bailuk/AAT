package ch.bailu.aat.gpx;

import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.attributes.Keys;
import ch.bailu.aat.preferences.SolidAutopause;

public class GpxListAttributes extends GpxAttributes {

    public static final GpxListAttributes NULL = factoryNull();

    public static final Keys KEYS = new Keys();

    public static final int INDEX_MAX_SPEED = KEYS.add("MaxSpeed");
    public static final int INDEX_AUTO_PAUSE = KEYS.add("AutoPause");
    public static final int INDEX_SLOPE = KEYS.add("Slope");
    public static final int INDEX_DESCEND = KEYS.add("Descend");
    public static final int INDEX_ASCEND = KEYS.add("Ascend");
    public static final int INDEX_CADENCE = KEYS.add("AverageCadence");
    public static final int INDEX_MAX_CADENCE = KEYS.add("MaxCadence");
    public static final int INDEX_TOTAL_CADENCE = KEYS.add("TotalCadence");
    public static final int INDEX_AVERAGE_HR = KEYS.add("AverageHR");
    public static final int INDEX_MAX_HR = KEYS.add("MaxHr");
    public static final int INDEX_HEART_BEATS = KEYS.add("HeartBeats");

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
    public String get(int key) {
        if (key == INDEX_MAX_SPEED) {
            return String.valueOf(maximumSpeed.get());

        } else if (key == INDEX_AUTO_PAUSE) {
            return String.valueOf(autoPause.get());

        } else if (key == INDEX_SLOPE) {
            return String.valueOf(altitudeDelta.getSlope());

        } else if (key == INDEX_DESCEND) {
            return String.valueOf(altitudeDelta.getDescend());

        } else if (key == INDEX_ASCEND) {
            return String.valueOf(altitudeDelta.getAscend());

        } else if (key == INDEX_CADENCE) {
            return String.valueOf(cadence.getAverageSpm());

        } else if (key == INDEX_TOTAL_CADENCE) {
            return String.valueOf(cadence.getTotalSamples());

        } else if (key == INDEX_MAX_CADENCE) {
            return String.valueOf(cadence.getMaxSpm());

        } else if (key == INDEX_HEART_BEATS) {
            return String.valueOf(hr.getTotalSamples());

        } else if (key == INDEX_AVERAGE_HR) {
            return String.valueOf(hr.getAverageSpm());

        } else if (key == INDEX_MAX_HR) {
            return String.valueOf(hr.getMaxSpm());
        }

        return NULL_VALUE;
    }

    @Override
    public boolean hasKey(int keyIndex) {
        return KEYS.hasKey(keyIndex);
    }


    @Override
    public float getAsFloat(int key) {
        if (key == INDEX_MAX_SPEED)
            return maximumSpeed.get();
        else if (key == INDEX_ASCEND)
            return altitudeDelta.getAscend();
        else if (key == INDEX_DESCEND)
            return altitudeDelta.getDescend();

        return super.getAsFloat(key);
    }


    @Override
    public long getAsLong(int key) {
        if (key == INDEX_AUTO_PAUSE) {
            return autoPause.get();
        }
        return super.getAsLong(key);
    }


    @Override
    public int getAsInteger(int key) {
        if (key == INDEX_SLOPE) {
            return altitudeDelta.getSlope();

        } else if (key == INDEX_DESCEND) {
            return altitudeDelta.getDescend();

        } else if (key == INDEX_ASCEND) {
            return altitudeDelta.getAscend();

        } else if (key == INDEX_CADENCE) {
            return cadence.getAverageSpm();

        } else if (key == INDEX_TOTAL_CADENCE) {
            return cadence.getTotalSamples();

        } else if (key == INDEX_MAX_CADENCE) {
            return cadence.getMaxSpm();

        } else if (key == INDEX_HEART_BEATS) {
            return hr.getTotalSamples();

        } else if (key == INDEX_AVERAGE_HR) {
            return hr.getAverageSpm();

        } else if (key == INDEX_MAX_HR) {
            return hr.getMaxSpm();
        }
        return super.getAsInteger(key);
    }


    @Override
    public int size() {
        return KEYS.size();
    }

    @Override
    public String getAt(int i) {
        return get(KEYS.getKeyIndex(i));
    }

    @Override
    public int getKeyAt(int i) {
        return KEYS.getKeyIndex(i);
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
