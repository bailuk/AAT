package ch.bailu.aat.preferences;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.GregorianCalendar;

import ch.bailu.aat_lib.description.FF;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class SolidDate extends SolidLong {
    private final String label;

    public SolidDate(StorageInterface s, String k, String l) {
        super(s, k);
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
        return FF.f().LOCAL_DATE.format(getValue());
    }


    @Override
    public String getLabel() {
        return label;
    }
}
