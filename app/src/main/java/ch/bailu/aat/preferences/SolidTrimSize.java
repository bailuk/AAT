package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;

public class SolidTrimSize extends SolidIndexList {

    public final static long KB=1024;
    public final static long MB=KB*KB;
    public final static long GB=KB*MB;

    public final static String sKB="KB";
    public final static String sMB="MB";
    public final static String sGB="GB";
    public final static String sB="B";



    private static class Entry {
        public final long size;
        public final String text;

        private Entry(long s) {
            size = s;
            text = buildSizeText(new StringBuilder(10), size).toString();
        }
    }

    public static StringBuilder buildSizeText(StringBuilder b, long size) {
        if (size >= GB) {
            b.append(size/GB);
            b.append(' ');
            b.append(sGB);

        } else if (size >= MB) {
            b.append(size/MB);
            b.append(' ');
            b.append(sMB);

        } else if (size >= KB) {
            b.append(size/KB);
            b.append(' ');
            b.append(sKB);

        } else {
            b.append(size);
            b.append(' ');
            b.append(sB);

        }
        return b;
    }


    private static final Entry[] entries = {
            new Entry(16 * GB),
            new Entry(8 * GB),
            new Entry(4 * GB),
            new Entry(2 * GB),
            new Entry(1 * GB),
            new Entry(500 * MB),
            new Entry(200 * MB),
            new Entry(100 * MB),
            new Entry(50 * MB),
    };


    public SolidTrimSize(Context context) {
        super(Storage.global(context), SolidTrimSize.class.getSimpleName());
    }

    @Override
    public int length() {
        return entries.length;
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_trim_size);
    }

    @Override
    public String getValueAsString(int i) {
        return entries[i].text;
    }

    public long getValue() {
        return entries[getIndex()].size;
    }

}
