package ch.bailu.aat.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class AppLayout {
    private static int width=0,height=0;
    
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
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }
    

    @SuppressWarnings("deprecation")
    public static void updateMeasurement(Context context) {
        if (height==0) height = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        if (width==0)  width = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    
    public static int getScreenSmallSide(Context context) {
        updateMeasurement(context);
        return Math.min(width, height);
    }
    
    public static int getScreenLargeSide(Context context) {
        updateMeasurement(context);
        return Math.max(width, height);
    }
    
    
    
    public static int getOrientation(Context c) {
        return c.getResources().getConfiguration().orientation;
    }
        
    public static void reportMeasurement(int w, int h) {
        width=w;
        height=h;
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
 