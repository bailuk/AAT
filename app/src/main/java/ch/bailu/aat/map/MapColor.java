package ch.bailu.aat.map;


import android.graphics.Color;

import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;

public final class MapColor {

    private static final UiTheme theme = AppTheme.bar;


    public static final int ALPHA_HIGH = 50;
    public static final int ALPHA_LOW = 200;



    public final static int LIGHT = setAlpha(Color.WHITE, ALPHA_LOW);
    public final static int MEDIUM = setAlpha(Color.BLACK, ALPHA_HIGH);
    public final static int DARK = setAlpha(Color.BLACK, ALPHA_LOW);




    public final static int NODE_NEUTRAL = setAlpha(Color.LTGRAY, ALPHA_LOW);
    public final static int NODE_SELECTED = LIGHT;


    public final static int GRID = Color.GRAY;
    public final static int EDGE = Color.BLACK; //AppTheme.getAltBackgroundColor();
    public final static int TEXT = Color.BLACK;
    //public static final int LEGEND_TEXT = Color.BLACK;


    static public int setAlpha(int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }


    public static int setValue(int color, float value) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = value;
        return Color.HSVToColor(hsv);
    }


    public static int setSaturation(int color, float saturation) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[1] = saturation;
        return Color.HSVToColor(hsv);
    }



    public static int getColorFromIID(int iid) {
        final int OVERLAY_COUNT = AppTheme.OVERLAY_COLOR.length;

        if (iid== InfoID.TRACKER)
            return theme.getHighlightColor();

        if (iid >= InfoID.OVERLAY && iid < InfoID.OVERLAY + OVERLAY_COUNT) {
            int slot =  iid - InfoID.OVERLAY;

            return AppTheme.OVERLAY_COLOR[slot];
        }

        if (iid == InfoID.EDITOR_DRAFT)
            return AppTheme.COLOR_GREEN;

        if (iid == InfoID.EDITOR_OVERLAY)
            return Color.MAGENTA; //AppTheme.getHighlightColor3();


        if (iid == InfoID.FILEVIEW)
            return AppTheme.COLOR_BLUE;



        return theme.getHighlightColor();
    }


    public static int toDarkTransparent(int color) {

        color = setValue(color, 0.30f);
        color = setAlpha(color, ALPHA_LOW);
        return color;
    }

    public static int toLightTransparent(int color) {

        color = setSaturation(color, 0.25f);
        color = setAlpha(color, ALPHA_LOW);
        return color;
    }

}
