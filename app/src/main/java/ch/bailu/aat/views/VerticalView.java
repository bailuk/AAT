package ch.bailu.aat.views;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;

public class VerticalView extends TrackDescriptionView {

    private final TrackDescriptionView[] viewList;


    public VerticalView(Context context, String key, int filter, TrackDescriptionView[] vl) {
        super(context, key, filter);
        viewList = vl;
        
        for (TrackDescriptionView view: viewList) {
            addView(view);
        }
    }


    

    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        final int width = MeasureSpec.getSize(wSpec);
        final int height = MeasureSpec.getSize(hSpec);

        if (viewList.length > 0) {
            wSpec = MeasureSpec.makeMeasureSpec (width,  MeasureSpec.EXACTLY);
            hSpec = MeasureSpec.makeMeasureSpec (height/viewList.length, MeasureSpec.EXACTLY);

            for (TrackDescriptionView view : viewList) {
                view.measure(wSpec, hSpec);
            }
        }

        setMeasuredDimension(width, height);
    }    

    
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (viewList.length>0) {
            final int height=(b-t) / viewList.length;

            r=r-l;
            l=t=0;

            for (TrackDescriptionView view : viewList) {
                b=t+height;
                
                view.layout(l, t, r, b);
                
                t+=height;
            }
        }
    }


    @Override
    public void updateGpxContent(GpxInformation info) {
        if (filter.pass(info)) {
            for (int i = 0; i< viewList.length; i++) viewList[i].updateGpxContent(info);
        }
    }
}
