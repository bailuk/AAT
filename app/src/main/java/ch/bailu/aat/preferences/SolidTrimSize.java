package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.util.fs.JFile;

public class SolidTrimSize extends SolidIndexList {

    private static class Entry {
        public final long size;
        public final String text;

        private Entry(long s) {
            size = s;
            text = JFile.reportFileSize(new StringBuilder(), size).toString();
        }
    }




    private static final Entry[] entries = {
            new Entry(16 * JFile.GB),
            new Entry(8 * JFile.GB),
            new Entry(4 * JFile.GB),
            new Entry(2 * JFile.GB),
            new Entry(1 * JFile.GB),
            new Entry(500 * JFile.MB),
            new Entry(200 * JFile.MB),
            new Entry(100 * JFile.MB),
            new Entry(50 * JFile.MB),
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
