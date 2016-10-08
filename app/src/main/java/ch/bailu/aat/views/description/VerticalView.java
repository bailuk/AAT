package ch.bailu.aat.views.description;

import android.content.Context;
import android.view.View;

import ch.bailu.aat.description.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;

public class VerticalView extends TrackDescriptionView {

    private final View[] views;
    private final OnContentUpdatedInterface[] targets;


    public VerticalView(Context context, String key, int filter, TrackDescriptionView[] vl) {
        this(context, key, filter, vl, vl);
    }

    public VerticalView(Context context,
                        String key,
                        int filter,
                        View[] v,
                        OnContentUpdatedInterface[] t) {
        super(context, key, filter);
        views = v;
        targets = t;

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


    @Override
    public void updateGpxContent(GpxInformation info) {
        if (filter.pass(info)) {
            for (int i = 0; i< targets.length; i++) targets[i].updateGpxContent(info);
        }
    }
}
