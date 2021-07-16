package ch.bailu.aat.map.tile;

import android.graphics.Paint;

public class TileFlags {
    /**
     * Paint.FILTER_BITMAP_FLAG -> slow drawing
     */
    public static final int SCALE_FLAGS = Paint.FILTER_BITMAP_FLAG;


    /**
     * Non-Transparent: Config.RGB_565   -> 2 bytes per pixel, slow drawing
     * Transparent:     Config.ARGB_8888 -> 4 bytes per pixel, fast drawing
     */
    public final static boolean ALWAYS_TRANSPARENT = false;
}
