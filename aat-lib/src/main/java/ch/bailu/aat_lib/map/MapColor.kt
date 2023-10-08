package ch.bailu.aat_lib.map;


import ch.bailu.aat_lib.app.AppColor;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.lib.color.ARGB;
import ch.bailu.aat_lib.lib.color.HSV;

public final class MapColor {

    //private static final UiTheme theme = AppTheme.bar;


    public static final int ALPHA_HIGH = 50;
    public static final int ALPHA_LOW = 200;



    public final static int LIGHT = setAlpha(ARGB.WHITE, ALPHA_LOW);
    public final static int MEDIUM = setAlpha(ARGB.BLACK, ALPHA_HIGH);
    public final static int DARK = setAlpha(ARGB.BLACK, ALPHA_LOW);




    public final static int NODE_NEUTRAL = setAlpha(ARGB.LTGRAY, ALPHA_LOW);
    public final static int NODE_SELECTED = LIGHT;


    public final static int GRID = ARGB.GRAY;
    public final static int EDGE = ARGB.BLACK; //AppTheme.getAltBackgroundColor();
    public final static int TEXT = ARGB.BLACK;
    //public static final int LEGEND_TEXT = Color.BLACK;


    static public int setAlpha(int color, int alpha) {
        return new ARGB(alpha, ARGB.red(color), ARGB.green(color), ARGB.blue(color)).toInt();
    }


    public static int setValue(int color, float value) {
        HSV hsv= new HSV(new ARGB(color));
        hsv.setValue(value);
        return hsv.toInt();
    }


    public static int setSaturation(int color, float saturation) {
        HSV hsv = new HSV(new ARGB(color));
        hsv.setSaturation(saturation);
        return hsv.toInt();
    }



    public static int getColorFromIID(int iid) {
        final int OVERLAY_COUNT = AppColor.OVERLAY_COLOR.length;

        if (iid== InfoID.TRACKER)
            return AppColor.HL_ORANGE;

        if (iid >= InfoID.OVERLAY && iid < InfoID.OVERLAY + OVERLAY_COUNT) {
            int slot =  iid - InfoID.OVERLAY;

            return AppColor.OVERLAY_COLOR[slot];
        }

        if (iid == InfoID.EDITOR_DRAFT)
            return AppColor.HL_GREEN;

        if (iid == InfoID.EDITOR_OVERLAY)
            return ARGB.MAGENTA; //AppTheme.getHighlightColor3();


        if (iid == InfoID.FILE_VIEW)
            return AppColor.HL_BLUE;



        return AppColor.HL_ORANGE;
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
