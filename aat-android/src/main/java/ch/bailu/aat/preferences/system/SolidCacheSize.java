package ch.bailu.aat.preferences.system;

import ch.bailu.aat.R;
import ch.bailu.aat.util.MemSize;
import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidCacheSize extends SolidIndexList {
    final static String KEY = "cache_size";

    private static long[] sizes;


    public SolidCacheSize(StorageInterface s) {
        super(s, KEY);

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
        return Res.str().p_cache_size();
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
