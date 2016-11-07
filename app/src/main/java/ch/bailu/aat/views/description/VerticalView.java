package ch.bailu.aat.views.description;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class VerticalView extends ViewGroup {

    private final View[] views;



    public VerticalView(Context context, View[] children) {
        super(context);
        views = children;

        for (View view: views) {
            addView(view);
        }
    }



    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        final int width = MeasureSpec.getSize(wSpec);
        final int height = MeasureSpec.getSize(hSpec);

        if (views.length > 0) {
            wSpec = MeasureSpec.makeMeasureSpec (width,  MeasureSpec.EXACTLY);
            hSpec = MeasureSpec.makeMeasureSpec (height/views.length, MeasureSpec.EXACTLY);

            for (View view : views) {
                view.measure(wSpec, hSpec);
            }
        }

        setMeasuredDimension(width, height);
    }    

    
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (views.length>0) {
            final int height=(b-t) / views.length;

            r=r-l;
            l=t=0;

            for (View view : views) {
                b=t+height;
                
                view.layout(l, t, r, b);
                
                t+=height;
            }
        }
    }
}
