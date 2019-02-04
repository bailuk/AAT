package ch.bailu.aat.gpx;

import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.attributes.Keys;
import ch.bailu.aat.services.sensor.attributes.CadenceSpeedAttributes;
import ch.bailu.aat.services.sensor.attributes.HeartRateAttributes;
import ch.bailu.util_java.util.Objects;

public class SampleRate {

    public final static int[] KEYS_BPM = {
            HeartRateAttributes.KEY_INDEX_BPM,
            Keys.toIndex("bpm"),
    };

    public final static int[] KEYS_CADENCE = {
            CadenceSpeedAttributes.KEY_INDEX_CRANK_RPM,
            Keys.toIndex("rpm"),
    };

    private final int KEY[];

    private long sampleTimeMillis = 0L;
    private long totalTimeMillis = 0L;


    private long totalSamples60KM = 0;


    private SampleRate(int... key) {
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


    public static final SampleRate NULL = new SampleRate() {
        @Override
        public void add(GpxPointNode p) {};
    };



    public void add(GpxPointNode p) {
        GpxAttributes attr = p.getAttributes();

        sampleTimeMillis += p.getTimeDelta();
        totalTimeMillis += p.getTimeDelta();

        final int spm = getValue(attr, KEY);

        if (spm > 0) {
            long bpSample60KM = sampleTimeMillis * spm;

            totalSamples60KM += bpSample60KM;

            sampleTimeMillis = 0L;
        }


    }





    public static int getValue(GpxAttributes attr, int... keys) {
        int r  = 0;

        for (int k : keys) {
            if (attr.hasKey(k)) {
                r = attr.getAsInteger(k);
                if (r > 0) break;
            }
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
