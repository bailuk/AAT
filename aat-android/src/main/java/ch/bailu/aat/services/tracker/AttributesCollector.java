package ch.bailu.aat.services.tracker;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.attributes.GpxAttributesNull;
import ch.bailu.aat.gpx.attributes.GpxAttributesStatic;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.SensorService;
import ch.bailu.aat.services.sensor.attributes.CadenceSpeedAttributes;
import ch.bailu.aat.services.sensor.attributes.HeartRateAttributes;
import ch.bailu.aat.services.sensor.attributes.PowerAttributes;
import ch.bailu.aat.services.sensor.attributes.StepCounterAttributes;

public final class AttributesCollector {
    private final static long LOG_INTERVAL = 0;
    private final static long SHORT_TIMEOUT = 2 * 1000;
    private final static long LONG_TIMEOUT = 10 * 1000;

    private long lastLog = System.currentTimeMillis();


    private final Collector[] collectors = new Collector[]{
            new Collector(InfoID.HEART_RATE_SENSOR, HeartRateAttributes.KEY_INDEX_BPM,
                    SHORT_TIMEOUT),

            new Collector(InfoID.CADENCE_SENSOR, CadenceSpeedAttributes.KEY_INDEX_CRANK_RPM,
                    SHORT_TIMEOUT),

            new Collector(InfoID.POWER_SENSOR, PowerAttributes.KEY_INDEX_POWER,
                    SHORT_TIMEOUT),

            new Collector(InfoID.STEP_COUNTER_SENSOR, StepCounterAttributes.KEY_INDEX_STEPS_RATE,
                    LONG_TIMEOUT),

            new StepsTotalCollector()
    };


    public GpxAttributes collect(ServiceContext scontext) {
        GpxAttributes attr = null;
        final long time = System.currentTimeMillis();

        if ((time - lastLog) >= LOG_INTERVAL) {
            lastLog = time;

            for (Collector c : collectors) {
                attr = c.collect(scontext, attr, time);
            }
        }

        if (attr == null) attr = GpxAttributesNull.NULL;
        return attr;
    }


    private static class Collector {
        private final int keyIndex;
        private final int infoID;
        private final long maxAge;

        private GpxInformation lastInfo;



        public Collector(int infoID, int keyIndex, long maxAge) {

            this.keyIndex = keyIndex;
            this.infoID = infoID;
            this.maxAge = maxAge;
        }

        public GpxAttributes collect(ServiceContext scontext, GpxAttributes target, long time) {
            final SensorService s = scontext.getSensorService();

            GpxInformation source = s.getInformationOrNull(infoID);

            if (source != null && source != lastInfo) {
                lastInfo = source;

                target = addAttribute(target, source, keyIndex, time);
            }
            return target;
        }


        protected GpxAttributes addAttribute(GpxAttributes target, GpxInformation source, int keyIndex, long time) {
            if (source != null && (time - source.getTimeStamp()) < maxAge) {
                target = addAttribute(target, source.getAttributes(), keyIndex);
            }
            return target;
        }

        protected GpxAttributes addAttribute(GpxAttributes target, GpxAttributes source, int keyIndex) {
            if (source.hasKey(keyIndex)) {

                target = addAttributeHaveKey(getTargetNotNull(target), source, keyIndex);
            }
            return target;
        }

        protected GpxAttributes addAttributeHaveKey(GpxAttributes target, GpxAttributes source, int keyIndex) {
            final String value = source.get(keyIndex);

            if (value.length() > 0) {
                target.put(keyIndex, value);
            }
            return target;
        }

        protected GpxAttributes getTargetNotNull(GpxAttributes target) {
            if (target == null) return new GpxAttributesStatic();
            return target;
        }
    }


    private final class StepsTotalCollector extends Collector {
        private int base = -1;

        public StepsTotalCollector() {
            super(InfoID.STEP_COUNTER_SENSOR,
                    StepCounterAttributes.KEY_INDEX_STEPS_TOTAL, LONG_TIMEOUT);
        }


        @Override
        protected GpxAttributes addAttributeHaveKey(GpxAttributes target, GpxAttributes source,
                                                    int keyIndex) {
            int value = source.getAsInteger(keyIndex);

            if (base == -1)
                base = value;

            value = value - base;

            target.put(keyIndex, String.valueOf(value));
            return target;
        }
    }
}
