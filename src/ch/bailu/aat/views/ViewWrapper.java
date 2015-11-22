package ch.bailu.aat.views;

import ch.bailu.aat.gpx.GpxInformation;
import android.view.View;

public class ViewWrapper extends TrackDescriptionView {

    private final View view;
    
    public ViewWrapper(View v) {
        super(v.getContext(), DEFAULT_SOLID_KEY,INFO_ID_ALL);
        view = v;
        addView(view);
    }

    
    @Override
    public void cleanUp() {
        
    }



    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        // As big as possible
        wSpec  = MeasureSpec.makeMeasureSpec (MeasureSpec.getSize(wSpec),  MeasureSpec.EXACTLY);
        hSpec  = MeasureSpec.makeMeasureSpec (MeasureSpec.getSize(hSpec),  MeasureSpec.EXACTLY);

        view.measure(wSpec, hSpec);
        setMeasuredDimension(wSpec, hSpec);
    }
    
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        view.layout(0, 0, r-l, b-t);
    }


    @Override
    public void updateGpxContent(GpxInformation info) {}

}
