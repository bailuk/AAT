package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.util.MemSize;

public class SolidTrimSize extends SolidIndexList {

    private static class Entry {
        public final long size;
        public final String text;

        private Entry(long s) {
            size = s;
            text = MemSize.describe(new StringBuilder(), size).toString();
        }
    }




    private static final Entry[] entries = {
            new Entry(16 * MemSize.GB),
            new Entry(8 * MemSize.GB),
            new Entry(4 * MemSize.GB),
            new Entry(2 * MemSize.GB),
            new Entry(1 * MemSize.GB),
            new Entry(500 * MemSize.MB),
            new Entry(200 * MemSize.MB),
            new Entry(100 * MemSize.MB),
            new Entry(50 * MemSize.MB),
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
