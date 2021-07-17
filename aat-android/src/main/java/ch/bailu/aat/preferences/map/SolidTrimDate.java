package ch.bailu.aat.preferences.map;

import android.content.Context;
import android.text.format.DateUtils;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.resources.Res;

public class SolidTrimDate extends SolidIndexList {


    private class Entry {
        public final long age;
        public final String name;

        private Entry(long s) {
            age = s;
            name = describe(s);
        }
    }

    public String describe(long size) {
        String s;

        if (size >= DateUtils.YEAR_IN_MILLIS) {
            size=size/ DateUtils.YEAR_IN_MILLIS;
            if (size == 1)
                s = Res.str().p_trim_year();
            else
                s = Res.str().p_trim_years();

        } else if (size >= DateUtils.DAY_IN_MILLIS*30) {
            size =size / (DateUtils.DAY_IN_MILLIS*30);
            if (size == 1)
                s = Res.str().p_trim_month();
            else
                s = Res.str().p_trim_months();

        } else  {
            size=size / DateUtils.DAY_IN_MILLIS;
            s = Res.str().p_trim_days();

        }

        return size + " " + s;
    }


    private final Entry[] entries = {
            new Entry(2L * DateUtils.YEAR_IN_MILLIS),
            new Entry(1L * DateUtils.YEAR_IN_MILLIS),
            new Entry(6L * 30L * DateUtils.DAY_IN_MILLIS),
            new Entry(3L * 30L * DateUtils.DAY_IN_MILLIS),
            new Entry(2L * 30L * DateUtils.DAY_IN_MILLIS),
            new Entry(1L * 30L * DateUtils.DAY_IN_MILLIS),
            new Entry(2L * 7L * DateUtils.DAY_IN_MILLIS),
            new Entry(1L * 7L * DateUtils.DAY_IN_MILLIS),
    };



    public SolidTrimDate(Context context) {
        super(new Storage(context), SolidTrimDate.class.getSimpleName());
    }


    @Override
    public String getLabel() {
        return Res.str().p_trim_age();
    }


    @Override
    public int length() {
        return entries.length;
    }

    @Override
    public String getValueAsString(int i) {
        return entries[i].name;
    }


    public long getValue() {
        return entries[getIndex()].age;
    }

}
