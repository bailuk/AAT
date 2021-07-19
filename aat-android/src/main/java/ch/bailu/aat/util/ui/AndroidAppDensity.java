package ch.bailu.aat.util.ui;

import android.content.Context;
import android.util.DisplayMetrics;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.map.AppDensity;

public class AndroidAppDensity extends AppDensity {
    private final float density;
    private final float scaledDensity;


    public AndroidAppDensity(ServiceContext scontext) {
        this(scontext.getContext());
    }

    public AndroidAppDensity(Context context) {
        this(context.getResources().getDisplayMetrics());
    }

    public AndroidAppDensity(DisplayMetrics metrics) {
        this(metrics.density, metrics.scaledDensity);
    }

    public AndroidAppDensity() {
        density=1;
        scaledDensity=1;
    }

    public AndroidAppDensity(float d, float sd) {
        density=d;
        scaledDensity=sd;
    }

    @Override
    public float toPixel_f(float diPixel) {
        return density * diPixel;
    }

    @Override
    public float toPixelScaled_f(float diPixel) {
        return scaledDensity * diPixel;
    }

    @Override
    public int toPixel_i(float diPixel) {
        return (int) (toPixel_f(diPixel)+0.5f);
    }

    @Override
    public int toPixel_i(float diPixel, int min) {
        return Math.max(min, toPixel_i(diPixel));
    }

    @Override
    public int toDensityIndependentPixel(float pixel) {
        return (int) (pixel / density);
    }
}
