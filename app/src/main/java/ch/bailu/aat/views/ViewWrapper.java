package ch.bailu.aat.views;

import ch.bailu.aat.gpx.GpxInformation;
import android.view.View;

public class ViewWrapper extends TrackDescriptionView {

    private final View view;
    
    public ViewWrapper(View v) {
        super(v.getContext(), DEFAULT_SOLID_KEY,GpxInformation.ID.INFO_ID_ALL);
        view = v;
        addView(view);
    }

    
   


    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        // As big as possible
        //wSpec  = MeasureSpec.makeMeasureSpec (MeasureSpec.getSize(wSpec),  MeasureSpec.AT_MOST);
        //hSpec  = MeasureSpec.makeMeasureSpec (MeasureSpec.getSize(hSpec),  MeasureSpec.AT_MOST);

        
        view.measure(wSpec, hSpec);

        /*
        wSpec  = MeasureSpec.makeMeasureSpec (view.getMeasuredWidth(),  MeasureSpec.EXACTLY);
        hSpec  = MeasureSpec.makeMeasureSpec (view.getMeasuredHeight(),  MeasureSpec.EXACTLY);
        */

        setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
    }
    
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        view.layout(0, 0, r-l, b-t);
    }


    @Override
    public void updateGpxContent(GpxInformation info) {}

}
