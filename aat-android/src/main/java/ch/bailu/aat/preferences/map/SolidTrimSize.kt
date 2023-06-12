package ch.bailu.aat.preferences.map;

import android.content.Context;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.util.MemSize;
import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.resources.Res;

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
            new Entry(16L * MemSize.GB),
            new Entry(8L * MemSize.GB),
            new Entry(4L * MemSize.GB),
            new Entry(2L * MemSize.GB),
            new Entry(1L * MemSize.GB),
            new Entry(500L * MemSize.MB),
            new Entry(200L * MemSize.MB),
            new Entry(100L * MemSize.MB),
            new Entry(50L * MemSize.MB),
    };


    public SolidTrimSize(Context context) {
        super(new Storage(context), SolidTrimSize.class.getSimpleName());
    }

    @Override
    public int length() {
        return entries.length;
    }


    @Override
    public String getLabel() {
        return Res.str().p_trim_size();
    }

    @Override
    public String getValueAsString(int i) {
        return entries[i].text;
    }

    public long getValue() {
        return entries[getIndex()].size;
    }

}
