package ch.bailu.aat_lib.util;

public class DateUtil {

    public static final long SECONDS_IN_MINUTE = 60;
    public static final long MINUTES_IN_HOUR = 60;
    public static final long HOURS_IN_DAY = 24;
    public static final long DAYS_IN_YEAR = 365;
    public static final long MILLIS_IN_SECOND = 1000;

    public static final long MILLIS_IN_HOUR = MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLIS_IN_SECOND;
    public static final long MILLIS_IN_DAY = MILLIS_IN_HOUR * HOURS_IN_DAY;
    public static final long MILLIS_IN_YEAR = MILLIS_IN_DAY * DAYS_IN_YEAR;
}
