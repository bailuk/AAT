package ch.bailu.aat.preferences;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Date;

public class SolidTrimDate extends SolidIndexList {


    private static class Entry {
        public final long age;
        public final String name;

        private Entry(long s) {
            age = s;
            name = describe(s);
        }
    }

    public static String describe(long size) {
        String s;

        if (size >= DateUtils.YEAR_IN_MILLIS) {
            s="Years*";
            size=size/ DateUtils.YEAR_IN_MILLIS;
        } else if (size >= DateUtils.DAY_IN_MILLIS*30) {
            s="Months*";
            size =size / (DateUtils.DAY_IN_MILLIS*30);

        } else  {
            s="Days*";
            size=size / DateUtils.DAY_IN_MILLIS;
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
        return "Trim Age*";
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
