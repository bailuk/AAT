package ch.bailu.aat_lib.description;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

public class FF {


    public final DateFormat LOCAL_DATE_TIME = DateFormat.getDateTimeInstance(
            DateFormat.LONG, DateFormat.LONG);

    public final DateFormat LOCAL_TIME = DateFormat.getTimeInstance();

    public final DateFormat LOCAL_DATE = DateFormat.getDateInstance();


    public final DecimalFormat N = new DecimalFormat("0");
    public final DecimalFormat N1 = new DecimalFormat("0.0");
    public final DecimalFormat N2 = new DecimalFormat("0.00");
    public final DecimalFormat N3 = new DecimalFormat("0.000");
    public final DecimalFormat N6 = new DecimalFormat("0.000000");


    public final DecimalFormat N3_3 = new DecimalFormat("000.000");

    private FF() {}

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
