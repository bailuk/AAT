package ch.bailu.aat.util.ui;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.StateSet;
import android.view.View;

public class AppTheme {

    public static final int HL_ORANGE = 0xffff6600;
    public static final int HL_GREEN = 0xffccff00;
    public static final int HL_BLUE = 0xff00d8ff;

    /**
     *  0xAARRGGBB
     *  A = Alpha
     *  R = Red
     *  G = Green
     *  B = Blue
     */
    public static final int[] OVERLAY_COLOR = {
            0xffff939f,
            0xffff94fd,
            0xff94ffb7,
            0xfffdff94,

            0x99ff3f54,
            0x99ff3ffc,
            0x993fff7d,
            0x99fcff3f,
    };


    // light & dark
/*
    public static final UiTheme bar = new UiThemeDark();
    public static final UiTheme cockpit = new UiThemeDark();
    public static final UiTheme preferences = new UiThemeLightOrange();
    public static final UiTheme intro = new UiThemeLightOrange();
    public static final UiTheme doc = new UiThemeLightOrange();
    public static final UiTheme search = new UiThemeLightOrange();
    public static final UiTheme trackList = new UiThemeLightOrange();
    public static final UiTheme trackContent = new UiThemeLightOrange();
    public static final UiTheme filter = new UiThemeLightHeader();
    public static final UiTheme alt = new UiThemeDark();
*/

    // gray & green
    public static final UiTheme bar = new UiThemeDarkGray();
    public static final UiTheme cockpit = new UiThemeLightGray();
    public static final UiTheme preferences = new UiThemeLightGray();
    public static final UiTheme intro = new UiThemeLightGray();
    public static final UiTheme doc = new UiThemeLightGray();
    public static final UiTheme search = new UiThemeLightGray();
    public static final UiTheme trackList = new UiThemeLightGray();
    public static final UiTheme trackContent = new UiThemeLightGray();
    public static final UiTheme filter = new UiThemeLightGrayBlue();
    public static final UiTheme alt = new UiThemeDarkGray();


    // light & blue
    /*
    public static final UiTheme bar = new UiThemeLight();
    public static final UiTheme cockpit = new UiThemeLight();
    public static final UiTheme preferences = new UiThemeLight();
    public static final UiTheme intro = new UiThemeLight();
    public static final UiTheme doc = new UiThemeLight();
    public static final UiTheme search = new UiThemeLight();
    public static final UiTheme trackList = new UiThemeLight();
    public static final UiTheme trackContent = new UiThemeLight();
    public static final UiTheme filter = new UiThemeLight();
    public static final UiTheme alt = new UiThemeLight();
    */
    // dark classic
    /*
    public static final UiTheme bar = new UiThemeDark();
    public static final UiTheme cockpit = new UiThemeDark();
    public static final UiTheme preferences = new UiThemeDark();
    public static final UiTheme intro = new UiThemeDark();
    public static final UiTheme doc = new UiThemeLightOrange();
    public static final UiTheme search = new UiThemeDark();
    public static final UiTheme trackList = new UiThemeDark();
    public static final UiTheme trackContent = new UiThemeDark();
    public static final UiTheme filter = new UiThemeDark2();
    public static final UiTheme alt = new UiThemeDark2();
   */

    public static void padding(View view) {
        padding(view, 15);
    }


    public static void padding(View view, int p) {
        int padding = new AppDensity(view.getContext()).toPixel_i(p);
        view.setPadding(padding,padding,padding,padding);
    }

    public static void paddingV(View view, int p) {
        int padding = new AppDensity(view.getContext()).toPixel_i(p);
        view.setPadding(0,padding,0,padding);
    }



    public static Drawable getButtonDrawable(int def, int pressed) {
        final StateListDrawable result = new StateListDrawable();

        result.addState(new int[] { android.R.attr.state_pressed}, new ColorDrawable(pressed));
        result.addState(StateSet.WILD_CARD, new ColorDrawable(def));
        return result;
    }
}
