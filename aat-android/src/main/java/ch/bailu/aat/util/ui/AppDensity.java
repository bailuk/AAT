package ch.bailu.aat.util.ui;

import android.content.Context;
import android.util.DisplayMetrics;

import ch.bailu.aat.services.ServiceContext;

public class AppDensity {
    private final float density;
    private final float scaledDensity;


    public AppDensity(ServiceContext scontext) {
        this(scontext.getContext());
    }

    public AppDensity(Context context) {
        this(context.getResources().getDisplayMetrics());
    }

    public AppDensity(DisplayMetrics metrics) {
        this(metrics.density, metrics.scaledDensity);
    }

    public AppDensity() {
        density=1;
        scaledDensity=1;
    }

    public AppDensity(float d, float sd) {
        density=d;
        scaledDensity=sd;
    }


    public float toPixel_f(float diPixel) {
        return density * diPixel;
    }

    public float toPixelScaled_f(float diPixel) {
        return scaledDensity * diPixel;
    }


    public int toPixel_i(float diPixel) {
        return (int) (toPixel_f(diPixel)+0.5f);
    }

    public int toPixel_i(float diPixel, int min) {
        return Math.max(min, toPixel_i(diPixel));
    }

    public int toDensityIndependentPixel(float pixel) {
        return (int) (pixel / density);
    }
}
