package ch.bailu.aat.services.tracker;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxAttributesStatic;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.SensorService;
import ch.bailu.aat.services.sensor.attributes.CadenceSpeedAttributes;
import ch.bailu.aat.services.sensor.attributes.HeartRateAttributes;

public class AttributesCollector {
    public final static long LOG_INTERVAL = 3 * 1000;
    public final static long MAX_AGE = 2 * 1000;

    public long lastLog = System.currentTimeMillis();
    public long time = System.currentTimeMillis();




    public GpxAttributes collect(ServiceContext scontext) {
        GpxAttributes attr = null;
        time = System.currentTimeMillis();

        if ((time - lastLog) >= LOG_INTERVAL) {
            lastLog = time;

            final SensorService s = scontext.getSensorService();

            attr = addAttribute(attr, s.getInformation(InfoID.HEART_RATE_SENSOR),
                    HeartRateAttributes.KEY_INDEX_BPM);

            attr = addAttribute(attr, s.getInformation(InfoID.CADENCE_SENSOR),
                    CadenceSpeedAttributes.KEY_INDEX_CRANK_RPM);

        }

        if (attr == null) attr = GpxAttributes.NULL;
        return attr;
    }


    private GpxAttributes addAttribute(GpxAttributes target, GpxInformation source, int index) {
        if (source != null && (time - source.getTimeStamp()) < MAX_AGE) {
            target = addAttribute(target, source.getAttributes(), index);
        }
        return target;
    }


    private GpxAttributes addAttribute(GpxAttributes target, GpxAttributes source, int index) {
        if (source != null && source.size() > index) {
            final String key = source.getKey(index);
            final String value = source.getValue(index);

            if (key.length() > 0 && value.length() > 0 && !value.equals("0")) {

                if (target == null) target = new GpxAttributesStatic();

                target.put(key, value);
            }
        }
        return target;
    }
}
