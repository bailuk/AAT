package ch.bailu.aat.gpx;

import ch.bailu.aat.services.sensor.attributes.CadenceSpeedAttributes;
import ch.bailu.aat.services.sensor.attributes.HeartRateAttributes;
import ch.bailu.util_java.util.Objects;

public class SampleRate {

    private final static String[] KEYS_BPM = {
            HeartRateAttributes.KEYS[HeartRateAttributes.KEY_INDEX_BPM],
            "bpm"
    };

    private final static String[] KEYS_CADENCE = {
            CadenceSpeedAttributes.KEYS[CadenceSpeedAttributes.KEY_INDEX_CRANK_RPM],
            "rpm"
    };

    private final String KEY[];

    private long sampleTimeMillis = 0L;
    private long totalTimeMillis = 0L;


    private long totalSamples60KM = 0;


    private SampleRate(String... key) {
        KEY = key;
    }


    public static class HeartRate extends SampleRate {

        public HeartRate() {
            super(KEYS_BPM);
        }
    }


    public static class Cadence extends SampleRate {
        public Cadence() {
            super (KEYS_CADENCE);
        }
    }


    public static final SampleRate NULL = new SampleRate("") {
        @Override
        public void add(GpxPointNode p) {};
    };



    public void add(GpxPointNode p) {
        GpxAttributes attr = p.getAttributes();

        sampleTimeMillis += p.getTimeDelta();
        totalTimeMillis += p.getTimeDelta();

        if (attr.size() > 0) {
            int spm = getValue(attr);

            if (spm > 0) {
                long bpSample60KM = sampleTimeMillis * spm;

                totalSamples60KM += bpSample60KM;

                sampleTimeMillis = 0L;
            }
        }


    }

    private int getValue(GpxAttributes attr) {
        int r  = 0;
        for (String k : KEY) {
            r = Objects.toInt(attr.get(k));
            if (r != 0) break;
        }
        return r;
    }


    public int getTotalSamples() {
        return Math.round(totalSamples60KM / 60000);
    }


    public int getAverageSpm() {
        if (totalTimeMillis > 0)
            return Math.round(totalSamples60KM / totalTimeMillis);
        return 0;
    }
}
