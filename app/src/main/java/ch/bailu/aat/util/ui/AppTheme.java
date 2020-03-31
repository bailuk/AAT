package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.StateSet;
import android.view.View;
import android.widget.ListView;

public class AppTheme {

    public static final int COLOR_ORANGE=Color.rgb(0xff, 0x66, 0x00);
    public static final int COLOR_GREEN=Color.rgb(0xcc,0xff,0x00);
    public static final int COLOR_BLUE=Color.rgb(0x00,0xd8,0xff);
//    public static final int COLOR_BLUEGRAY=Color.rgb(44, 45, 63);


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


    public static final UiTheme bar = new UiThemeDark();
    public static final UiTheme cockpit = new UiThemeDark();
    public static final UiTheme preferences = new UiThemeLightOrange();
    public static final UiTheme intro = new UiThemeLightOrange();
    public static final UiTheme alt = new UiThemeDark();
    public static final UiTheme doc = new UiThemeLightOrange();
    public static final UiTheme search = new UiThemeLightOrange();
    public static final UiTheme trackList = new UiThemeLightOrange();
    public static final UiTheme trackContent = new UiThemeLightOrange();
    public static final UiTheme filter = new UiThemeLightHeader();

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
