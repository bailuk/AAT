package ch.bailu.aat_lib.description;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class TimeDescription extends LongDescription {

    @Override
    public String getLabel() {
        return Res.str().time();
    }

    @Override
    public String getUnit() {
        return "";
    }

    public String getValue() {
        return format(getCache());
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getTimeDelta());
    }

    private static final StringBuilder builder = new StringBuilder(10);

    public static String format(long time) {
        synchronized (builder) {
            builder.setLength(0);
            return format(builder, time).toString();
        }
    }

    public static StringBuilder format(StringBuilder out, long time) {
        int seconds, hours, minutes;

        // 1. calculate milliseconds to unit
        seconds = (int) (time / 1000);
        minutes = seconds / 60;
        hours = minutes / 60;

        // 2. cut away values that belong to a higher unit
        seconds -= minutes * 60;
        minutes -= hours * 60;

        appendValueAndDelimiter(out, hours);
        appendValueAndDelimiter(out, minutes);
        appendValue(out, seconds);
        return out;
    }

    private static void appendValueAndDelimiter(StringBuilder builder, int value) {
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
