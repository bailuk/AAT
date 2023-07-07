package ch.bailu.aat_lib.description;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class FF_GPX {
    public final DateFormat TIME =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT);

    public final DecimalFormat N =
            new DecimalFormat("0", new DecimalFormatSymbols(Locale.ROOT));

    public final DecimalFormat N1 =
            new DecimalFormat("0.0", new DecimalFormatSymbols(Locale.ROOT));

    public final DecimalFormat N6 =
            new DecimalFormat("0.000000", new DecimalFormatSymbols(Locale.ROOT));

    private FF_GPX() {
        final TimeZone UTC = TimeZone.getTimeZone("UTC");
        TIME.setTimeZone(UTC);
    }

    private static final ThreadLocal<FF_GPX> F = new ThreadLocal<FF_GPX>() {
        @Override
        public FF_GPX initialValue() {
            return new FF_GPX();
        }
    };

    public static FF_GPX f() {
        return F.get();
    }

}
