package ch.bailu.aat.views;

import android.content.Context;
import android.widget.LinearLayout;

public class VerticalLayoutView extends DescriptionViewGroup {

    private final LinearLayout layout;

    public VerticalLayoutView(Context context, String key, int filter, TrackDescriptionView[] viewList) {
        super(context, key, filter);
        init(viewList);

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        for (TrackDescriptionView view: viewList) 
            layout.addView(view);
        
        addView(layout);
    }




    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        int width = MeasureSpec.getSize(wSpec);
        int height=MeasureSpec.getSize(hSpec);
        wSpec  = MeasureSpec.makeMeasureSpec (width,  MeasureSpec.EXACTLY);
        hSpec  = MeasureSpec.makeMeasureSpec (height,  MeasureSpec.EXACTLY);
        setMeasuredDimension(wSpec, hSpec);
        
        layout.measure(wSpec, hSpec);
    }    



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layout.layout(0, 0, r-l, b-t);
    }
}
