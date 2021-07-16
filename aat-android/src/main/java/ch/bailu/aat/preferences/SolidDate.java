package ch.bailu.aat.preferences;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.GregorianCalendar;

public class SolidDate extends SolidLong {
    private final String label;

    public SolidDate(Context c, String k, String l) {
        super(c, k);
        label = l;
    }




    @Override
    public long getValue() {
        if (super.getValue() == 0) {
            return System.currentTimeMillis();
        }
        return super.getValue();
    }


    @Override
    public String getValueAsString() {
        final GregorianCalendar calendar=new GregorianCalendar();
        final java.text.DateFormat formater = DateFormat.getDateFormat(getContext());

        calendar.setTimeInMillis(getValue());

        return formater.format(calendar.getTime());
    }


    @Override
    public String getLabel() {
        return label;
    }
}
