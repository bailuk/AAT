package ch.bailu.aat.preferences;

import android.content.Context;

import org.mapsforge.map.layer.renderer.MapWorkerPool;

public class SolidRendererThreads extends SolidIndexList {

    private final static String KEY = "renderer_threads";
    private final int[] values = {1,2,3,4};

    public SolidRendererThreads(Context c) {
        super(Storage.global(c), KEY);
    }

    @Override
    public int length() {
        return values.length;
    }

    @Override
    protected String getValueAsString(int i) {
        return String.valueOf(values[i]);
    }

    @Override
    public String getLabel() {
        return "Renderer Threads*";
    }


    public int getValue() {
        return values[getIndex()];
    }

    public void set() {
        MapWorkerPool.NUMBER_OF_THREADS = getValue();
    }
}
