package ch.bailu.aat.util.ui;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.StateSet;
import android.view.View;

import ch.bailu.aat_lib.app.AppColor;

public class AppTheme {



    // light & dark
    private static final int hl_color = AppColor.HL_ORANGE;
    public static final UiTheme bar = new UiThemeDark(hl_color);
    public static final UiTheme cockpit = new UiThemeDark(hl_color);
    public static final UiTheme preferences = new UiThemeLightOrange(hl_color);
    public static final UiTheme intro = new UiThemeLightOrange(hl_color);
    public static final UiTheme doc = new UiThemeLightOrange(hl_color);
    public static final UiTheme search = new UiThemeLightOrange(hl_color);
    public static final UiTheme trackList = new UiThemeLightOrange(hl_color);
    public static final UiTheme trackContent = new UiThemeLightOrange(hl_color);
    public static final UiTheme filter = new UiThemeLightHeader(UiThemeLight.HL_BLUE);
    public static final UiTheme alt = new UiThemeDark(hl_color);


    // gray & green
    /*
    public static final UiTheme bar = new UiThemeDarkGray(AppTheme.HL_GREEN);
    public static final UiTheme cockpit = new UiThemeDarkGray(AppTheme.HL_GREEN);
    public static final UiTheme preferences = new UiThemeLightGray();
    public static final UiTheme intro = new UiThemeLightGray();
    public static final UiTheme doc = new UiThemeLightGray();
    public static final UiTheme search = new UiThemeLightGray();
    public static final UiTheme trackList = new UiThemeLightGray();
    public static final UiTheme trackContent = new UiThemeLightGray();
    public static final UiTheme filter = new UiThemeLightGrayBlue();
    public static final UiTheme alt = new UiThemeDarkGray(AppTheme.HL_GREEN);
*/

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
        int padding = new AndroidAppDensity(view.getContext()).toPixel_i(p);
        view.setPadding(padding,padding,padding,padding);
    }

    public static void paddingV(View view, int p) {
        int padding = new AndroidAppDensity(view.getContext()).toPixel_i(p);
        view.setPadding(0,padding,0,padding);
    }



    public static Drawable getButtonDrawable(int def, int pressed) {
        final StateListDrawable result = new StateListDrawable();

        result.addState(new int[] { android.R.attr.state_pressed}, new ColorDrawable(pressed));
        result.addState(StateSet.WILD_CARD, new ColorDrawable(def));
        return result;
    }
}
