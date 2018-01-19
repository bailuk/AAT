package ch.bailu.aat.util.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import ch.bailu.aat.R;
import ch.bailu.aat.views.SVGAssetView;

public class AppTheme {
    private static final float TEXT_SIZE=20f;

    private static final int COLOR_ORANGE=Color.rgb(0xff, 0x66, 0x00);
    private static final int COLOR_GREEN=Color.rgb(0xcc,0xff,0x00);
    private static final int COLOR_BLUE=Color.rgb(0x00,0xd8,0xff);
    private static final int COLOR_BLUEGRAY=Color.rgb(44, 45, 63);


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


    static public int getHighlightColor (){
        return COLOR_ORANGE;
    }

    static public int getAltBackgroundColor() {
        return COLOR_BLUEGRAY;
    }



    public final static String APP_LONG_NAME = "Android Activity Tracker";
    public final static String APP_SHORT_NAME = "aat";


    public static int getHighlightColor2() {
        return COLOR_GREEN;
    }

    public static int getHighlightColor3() {
        return COLOR_BLUE;
    }

    public static void themify(CheckBox b) {
        b.setTextColor(Color.WHITE);
        b.setTextSize(TEXT_SIZE);
    }


    public static void themify(Button w) {
        w.setBackgroundResource(R.drawable.button);
        w.setTextColor(Color.WHITE);
        w.setTextSize(TEXT_SIZE);
    }

    public static void themify(TextView label) {
        label.setTextSize(TEXT_SIZE);
        label.setTextColor(Color.WHITE);
    }





    public static void themify(ImageView w) {
        w.setBackgroundResource(R.drawable.button);
    }

    public static void themify(ListView list, int color) {
        int height = list.getDividerHeight();
        list.setDivider(new ColorDrawable(color));
        list.setDividerHeight(height);
    }


    public static void themify(LinearLayout bar) {
        bar.setBackgroundResource(R.drawable.button);
    }


}
