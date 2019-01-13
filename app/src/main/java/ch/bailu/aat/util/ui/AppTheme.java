package ch.bailu.aat.util.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import ch.bailu.aat.BuildConfig;
import ch.bailu.aat.R;

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


    static public int getHighlightColor () {
        return COLOR_ORANGE;
    }


    public static final UiTheme bar = new UiThemeDark();
    public static final UiTheme main = new UiThemeDark();
    public static final UiTheme alt = new UiThemeDark2();


    public static void themifyList(ListView list) {
        int height = list.getDividerHeight();
        list.setDivider(new ColorDrawable(getHighlightColor()));
        list.setDividerHeight(height);
    }
}
