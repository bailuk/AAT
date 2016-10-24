package ch.bailu.aat.helpers;

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


    public float toDPf(float pixel) {
        return density * pixel;
    }

    public float toSDPf(float pixel) {
        return scaledDensity * pixel;
    }


    public int toDPi(float pixel) {
        return (int) (toDPf(pixel)+0.5f);
    }

    public int toDPi(float pixel, int min) {
        return Math.max(min, toDPi(pixel));
    }
}
