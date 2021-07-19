package ch.bailu.aat.description;

import ch.bailu.aat_lib.description.FF;
import ch.bailu.aat_lib.description.FloatDescription;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidUnit;

public abstract class PaceDescription extends FloatDescription {
    private final SolidUnit sunit;

    @Override
    public String getUnit() {
        return sunit.getPaceUnit();
    }


    @Override
    public String getValue() {
        final float pace = getCache() * sunit.getPaceFactor();

        return getPaceTimeString(pace);
    }


    private String getPaceTimeString(float pace) {

        if (sunit.getIndex() == SolidUnit.SI) {
            return FF.f().N1.format(pace);
        }
        return format(pace);
    }

    public PaceDescription(StorageInterface s) {
        sunit = new SolidUnit(s);
    }

    public float speedToPace(float speed) {
        float pace = 0f;

        if (speed != 0f)
            pace = 1f / speed;

        return pace;
    }


    private static final StringBuilder builder = new StringBuilder(6);

    public static String format(float pace) {
        synchronized (builder) {
            builder.setLength(0);
            return format(builder, pace).toString();
        }
    }

    public static StringBuilder format(StringBuilder out, float pace) {
        int seconds, hours, minutes;

        // 1. calculate milliseconds to unit
        seconds = Math.round(pace);
        minutes = seconds / 60;
        hours = minutes / 60;

        // 2. cut away values that belong to a higher unit
        seconds -= minutes * 60;
        minutes -= hours * 60;

        //appendValueAndDelimer(out, hours);
        appendValueAndDelimer(out, minutes);
        appendValue(out, seconds);
        return out;
    }

    private static void appendValueAndDelimer(StringBuilder builder, int value) {
        appendValue(builder,value);
        builder.append(":");
    }

    private static void appendValue(StringBuilder builder, int value) {
        if (value < 10) {
            builder.append("0");
        }
        builder.append(value);
    }
}
