package ch.bailu.aat.map;


import android.graphics.Color;

import ch.bailu.aat.util.ui.AppTheme;

public class MapColor {

    public static final int ALPHA_HIGH = 50;
    public static final int ALPHA_LOW = 200;


    public final static int LIGHT = addAlpha(ALPHA_LOW, Color.WHITE);
    public final static int MEDIUM = addAlpha(ALPHA_HIGH, Color.BLACK);
    public final static int DARK = addAlpha(ALPHA_LOW, Color.BLACK);


    public final static int NODE_NEUTRAL = addAlpha(ALPHA_LOW, Color.LTGRAY);
    public final static int NODE_SELECTED = LIGHT;


    public final static int GRID = Color.GRAY;
    public final static int EDGE = AppTheme.getAltBackgroundColor();
    public final static int TEXT = Color.BLACK;


    static public int addAlpha(int alpha, int c) {
        return Color.argb(alpha, Color.red(c), Color.green(c), Color.blue(c));
    }



}
