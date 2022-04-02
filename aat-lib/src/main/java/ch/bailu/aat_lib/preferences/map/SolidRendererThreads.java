package ch.bailu.aat_lib.preferences.map;

import org.mapsforge.core.util.Parameters;

import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidRendererThreads extends SolidIndexList {

    private final static String KEY = "renderer_threads";
    private final static int[] values = {0,2,3,4,1};

    /**
     * This does not has to be user configurable.
     * Use numberOfBackgroundThreads() instead.
     */
    private SolidRendererThreads(StorageInterface storageInterface) {
        super(storageInterface, KEY);
        if (values[0] == 0) values[0] = numberOfBackgroundThreats();
    }


    public static int numberOfBackgroundThreats() {
        int result = numberOfCores()-1;
        result = Math.min(result, 3);
        result = Math.max(result, 1);
        return result;
    }


    public static int numberOfCores() {
        try {
           return Math.max(Runtime.getRuntime().availableProcessors(), 1);
        } catch (Exception e) {
            return 1;
        }
    }

    @Override
    public int length() {
        return values.length;
    }

    @Override
    protected String getValueAsString(int i) {
        return toDefaultString(String.valueOf(values[i]), i);
    }

    @Override
    public String getLabel() {
        return Res.str().p_render_threads();
    }


    public int getValue() {
        return values[getIndex()];
    }

    public static void set() {
        Parameters.NUMBER_OF_THREADS = numberOfBackgroundThreats();
    }

}
