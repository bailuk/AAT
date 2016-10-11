package ch.bailu.aat.preferences;

import android.content.Context;
import android.text.format.DateUtils;

import ch.bailu.aat.R;

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
                s = getContext().getString(R.string.p_trim_year);
            else
                s = getContext().getString(R.string.p_trim_years);

        } else if (size >= DateUtils.DAY_IN_MILLIS*30) {
            size =size / (DateUtils.DAY_IN_MILLIS*30);
            if (size == 1)
                s = getContext().getString(R.string.p_trim_month);
            else
                s = getContext().getString(R.string.p_trim_months);

        } else  {
            size=size / DateUtils.DAY_IN_MILLIS;
            s = getContext().getString(R.string.p_trim_days);

        }

        return size + " " + s;
    }


    private final Entry[] entries = {
            new Entry(2 * DateUtils.YEAR_IN_MILLIS),
            new Entry(1 * DateUtils.YEAR_IN_MILLIS),
            new Entry(6 * 30 * DateUtils.DAY_IN_MILLIS),
            new Entry(3 * 30 * DateUtils.DAY_IN_MILLIS),
            new Entry(2 * 30 * DateUtils.DAY_IN_MILLIS),
            new Entry(1 * 30 * DateUtils.DAY_IN_MILLIS),
            new Entry(2 * 7 * DateUtils.DAY_IN_MILLIS),
            new Entry(1 * 7 * DateUtils.DAY_IN_MILLIS),
    };



    public SolidTrimDate(Context context) {
        super(Storage.global(context), SolidTrimDate.class.getSimpleName());
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_trim_age);
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
