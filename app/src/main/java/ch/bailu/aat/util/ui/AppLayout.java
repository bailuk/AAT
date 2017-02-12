package ch.bailu.aat.util.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class AppLayout {
    private static final int BIG_BUTTON_SIZE=100;

    public final static int DEFAULT_VISIBLE_BUTTON_COUNT = 4;
    private final static int GPS_EXTRA_BUTTON_COUNT = DEFAULT_VISIBLE_BUTTON_COUNT +1;
    private final static int BACK_EXTRA_BUTTON_COUNT = GPS_EXTRA_BUTTON_COUNT+1;

    private static final Point size = new Point();


    public static void updateMeasurement(Context context) {

        if (size.x==0 || size.y==0) {
            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

            if (wm != null) {
                Display disp = wm.getDefaultDisplay();
                if (disp != null) {
                    if (android.os.Build.VERSION.SDK_INT < 13) {
                        getSizeSDK1(disp, size);
                    } else {
                        getSizeSDK13(disp, size);
                    }
                }
            }
        }
    }


    @TargetApi(13)
    public static void getSizeSDK13(Display disp, Point size) {
        disp.getSize(size);
    }


    @SuppressWarnings("deprecation")
    public static void getSizeSDK1(Display disp, Point size) {
        size.set(disp.getWidth(), disp.getHeight());
    }

    public static int getScreenSmallSide(Context context) {
        updateMeasurement(context);
        return Math.min(size.x, size.y);
    }

    public static int getScreenLargeSide(Context context) {
        updateMeasurement(context);
        return Math.max(size.x, size.y);
    }


    public static int getOrientation(Context c) {
        return c.getResources().getConfiguration().orientation;
    }

    public static int getOrientationAlongSmallSide(Context context) {
        if (getOrientation(context) == Configuration.ORIENTATION_LANDSCAPE)
            return LinearLayout.VERTICAL;

        return LinearLayout.HORIZONTAL;
    }

    public static int getOrientationAlongLargeSide(Context context) {
        if (getOrientationAlongSmallSide(context) == LinearLayout.VERTICAL)
            return LinearLayout.HORIZONTAL;
        return LinearLayout.VERTICAL;
    }


    public static int getBigButtonSize(Context context) {
        return getBigButtonSize(context, AppLayout.DEFAULT_VISIBLE_BUTTON_COUNT);
    }

    public static int getBigButtonSize(Context context, int buttonCount) {
        int big_button_size = new AppDensity(context).toDPi(BIG_BUTTON_SIZE);
        return Math.min(AppLayout.getScreenSmallSide(context) / buttonCount, big_button_size);
    }

    public static boolean haveExtraSpaceGps(Context context) {
        return getVisibleButtonCount(context) >= GPS_EXTRA_BUTTON_COUNT;
    }

    public static boolean haveExtraSpaceBack(Context context) {
        return getVisibleButtonCount(context) >= BACK_EXTRA_BUTTON_COUNT;
    }

    public static int getVisibleButtonCount(Context context) {
        int screen_size = getScreenSmallSide(context);
        int button_size = getBigButtonSize(context);

        return screen_size / button_size;
    }
}
 