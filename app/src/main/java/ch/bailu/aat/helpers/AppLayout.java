package ch.bailu.aat.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class AppLayout {
    private static final Point size = new Point();
    
    public static int getDimension (int spec, int preferred) {
        
        int specSize = MeasureSpec.getSize(spec);
        int measurement = 0;
        
        switch(MeasureSpec.getMode(spec))
        {
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }
    


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
        if (getOrientation(context) == Configuration.ORIENTATION_LANDSCAPE) return LinearLayout.VERTICAL;
        else return LinearLayout.HORIZONTAL;
    }
    
    public static int getOrientationAlongLargeSide(Context context) {
        if (getOrientationAlongSmallSide(context) == LinearLayout.VERTICAL) return LinearLayout.HORIZONTAL;
        return LinearLayout.VERTICAL;
    }
    
    public static float toPixel(Context context, float pixel) {
        return context.getResources().getDisplayMetrics().density*pixel;
    }
    
}
 