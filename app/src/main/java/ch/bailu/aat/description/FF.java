package ch.bailu.aat.description;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class FF {
    public final DateFormat GPX_TIME =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public final DateFormat LOCAL_DATE_TIME = DateFormat.getDateTimeInstance(
            DateFormat.LONG, DateFormat.LONG);

    public final DecimalFormat N = new DecimalFormat("0");
    public final DecimalFormat N1 = new DecimalFormat("0.0");
    public final DecimalFormat N2 = new DecimalFormat("0.00");
    public final DecimalFormat N3 = new DecimalFormat("0.000");
    public final DecimalFormat N6 = new DecimalFormat("0.000000");

    public final DecimalFormat N3_3 = new DecimalFormat("000.000");

    public FF() {
        final TimeZone UTC = TimeZone.getTimeZone("UTC");
        GPX_TIME.setTimeZone(UTC);
    }


    private static final ThreadLocal<FF> F = new ThreadLocal<FF>() {
        @Override
        public FF initialValue() {
            return new FF();
        }
    };

    public static FF f() {
        return F.get();
    }

}
