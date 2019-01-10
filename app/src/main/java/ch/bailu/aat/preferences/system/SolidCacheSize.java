package ch.bailu.aat.preferences.system;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidIndexList;
import ch.bailu.aat.util.MemSize;

public class SolidCacheSize extends SolidIndexList {
    final static String KEY = "cache_size";

    private static long[] sizes;


    public SolidCacheSize(Context c) {
        super(c, KEY);

        if (sizes == null) {
            long max = Runtime.getRuntime().maxMemory();

            sizes = new long[11];

            sizes[0] = MemSize.round(max / 5);
            sizes[sizes.length-1] = max;

            for (int i = sizes.length-2; i > 0; i--) {
                sizes[i] = MemSize.round(sizes[i+1] / 3 * 2);
            }
        }
    }


    @Override
    public int length() {
        return sizes.length;
    }

    @Override
    public String getLabel() {
        return getString(R.string.p_cache_size);
    }

    @Override
    protected String getValueAsString(int i) {
        StringBuilder b = new StringBuilder();

        MemSize.describe(b, sizes[i]);

        return toDefaultString(b.toString(), i);
    }


    public long getValueAsLong() {
        return sizes[getIndex()];
    }
}
