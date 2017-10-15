package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.util.MemSize;

public class SolidCacheSize extends SolidIndexList {
    final static String KEY = "cache_size";

    private static long[] sizes;


    public SolidCacheSize(Context c) {
        super(Storage.global(c), KEY);

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
        return "Cache Size*";
    }

    @Override
    protected String getValueAsString(int i) {
        StringBuilder b = new StringBuilder();

        MemSize.describe(b, sizes[i]);

        if (i == 0) {
            b.append(" ").append(getContext().getString(R.string.auto));
        }
        return b.toString();
    }


    public long getValueAsLong() {
        return sizes[getIndex()];
    }
}
