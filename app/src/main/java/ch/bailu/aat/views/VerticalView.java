package ch.bailu.aat.views;

import android.content.Context;

public class VerticalView extends DescriptionViewGroup {
    
    public VerticalView(Context context, String key, int filter, TrackDescriptionView[] viewList) {
        super(context, key, filter);
        init(viewList);
        
        for (TrackDescriptionView view: viewList) 
            addView(view);
    }


    
    
    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        if (getDescriptionCount()>0) {
            int width = MeasureSpec.getSize(wSpec);
            int height=MeasureSpec.getSize(hSpec);
        
            int hSpecChild  = 
                    MeasureSpec.makeMeasureSpec (height/getDescriptionCount(), MeasureSpec.EXACTLY);
            
            wSpec  = MeasureSpec.makeMeasureSpec (width,  MeasureSpec.EXACTLY);
            hSpec  = MeasureSpec.makeMeasureSpec (height,  MeasureSpec.EXACTLY);

            for (int i=0; i < getDescriptionCount(); i++) {
                getDescriptionView(i).measure(wSpec, hSpecChild);
            }

            setMeasuredDimension(wSpec, hSpec);
        }
    }    

    
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getDescriptionCount()>0) {
            int height=(b-t) / getDescriptionCount();
            
            l=t=0;
            
            for (int i=0; i < getDescriptionCount(); i++) {
                b=t+height;
                
                getDescriptionView(i).layout(l, t, r, b);
                
                t+=height;

            }
        }

    }

}
