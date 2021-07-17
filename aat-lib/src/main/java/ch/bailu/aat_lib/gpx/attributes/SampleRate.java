package ch.bailu.aat_lib.gpx.attributes;


import ch.bailu.aat_lib.gpx.GpxPointNode;

public class SampleRate extends GpxSubAttributes {

    private final int[] KEY;

    private long sampleTimeMillis = 0L;
    private long totalTimeMillis = 0L;


    private long totalSamples60KM = 0;

    private int maxSamplesPM = 0;


    private SampleRate( Keys keys, int... key) {
        super(keys);
        KEY = key;
    }


    @Override
    public String get(int key) {
        return String.valueOf(getAsInteger(key));
    }


    public static class StepsRate extends SampleRate {
        public final static int[] GPX_KEYS = {
                StepCounterAttributes.KEY_INDEX_STEPS_RATE,
        };

        private static final Keys KEYS = new Keys();

        public static final int INDEX_MAX_SPM = KEYS.add("MaxStepsRate");

        public StepsRate() {
            super(KEYS, GPX_KEYS);
        }

        @Override
        public int getAsInteger(int key) {

            if (key == INDEX_MAX_SPM) {
                return getMaxSpm();
            }
            return super.getAsInteger(key);
        }
    }


    public static class HeartRate extends SampleRate {
        public final static int[] GPX_KEYS = {
                HeartRateAttributes.KEY_INDEX_BPM,
                Keys.toIndex("bpm"),
                Keys.toIndex("HR")
        };


        private static final Keys KEYS = new Keys();

        public static final int INDEX_AVERAGE_HR = KEYS.add("HeartRate");
        public static final int INDEX_MAX_HR = KEYS.add("MaxHeartRate");
        public static final int INDEX_HEART_BEATS = KEYS.add("HeartBeats");

        public HeartRate() {
            super(KEYS, GPX_KEYS);
        }


        @Override
        public int getAsInteger(int key) {

            if (key == INDEX_HEART_BEATS) {
                return getTotalSamples();

            } else if (key == INDEX_AVERAGE_HR) {
                return getAverageSpm();

            } else if (key == INDEX_MAX_HR) {
                return getMaxSpm();
            }
            return super.getAsInteger(key);
        }

    }


    public static class Cadence extends SampleRate {
        public final static int[] GPX_KEYS = {
                CadenceSpeedAttributes.KEY_INDEX_CRANK_RPM,
                Keys.toIndex("rpm"),
        };

        private static final Keys KEYS = new Keys();

        public static final int INDEX_CADENCE = KEYS.add("Cadence");
        public static final int INDEX_MAX_CADENCE = KEYS.add("MaxCadence");
        public static final int INDEX_TOTAL_CADENCE = KEYS.add("TotalCadence");

        public Cadence() {
            super (KEYS, GPX_KEYS);
        }

        @Override
        public int getAsInteger(int key) {
            if (key == INDEX_CADENCE) {
                return getAverageSpm();

            } else if (key == INDEX_TOTAL_CADENCE) {
                return getTotalSamples();

            } else if (key == INDEX_MAX_CADENCE) {
                return getMaxSpm();

            }
            return super.getAsInteger(key);
        }

    }


    @Override
    public boolean update(GpxPointNode p, boolean autoPause) {
        if (!autoPause) {
            GpxAttributes attr = p.getAttributes();

            sampleTimeMillis += p.getTimeDelta();
            totalTimeMillis += p.getTimeDelta();

            final int spm = getValue(attr, KEY);

            if (spm > 0) {
                long bpSample60KM = sampleTimeMillis * spm;

                totalSamples60KM += bpSample60KM;

                sampleTimeMillis = 0L;

                if (spm > maxSamplesPM) {
                    maxSamplesPM = spm;
                }

            }
        }

        return autoPause;
    }





    public static int getValue(GpxAttributes attr, int... keys) {
        int r  = 0;

        for (int k : keys) {
            if (attr.hasKey(k)) {
                r = attr.getAsInteger(k);
                if (r > 0) {
                    break;
                }
            }
        }
        return r;
    }

    public int getTotalSamples() {
        return Math.round(totalSamples60KM / 60000f);
    }


    public int getAverageSpm() {
        if (totalTimeMillis > 0)
            return (int) Math.round(totalSamples60KM / (double)totalTimeMillis);
        return 0;
    }

    public int getMaxSpm() {
        return maxSamplesPM;
    }
}
