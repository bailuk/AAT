package ch.bailu.aat.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ch.bailu.aat.views.preferences.VerticalScrollView;

public class VerticalLayoutView extends DescriptionViewGroup {

    private final VerticalScrollView layout;

    public VerticalLayoutView(Context context, String key, int filter, TrackDescriptionView[] viewList) {
        super(context, key, filter);
        init(viewList);

        layout = new VerticalScrollView(context);

        for (TrackDescriptionView view: viewList) 
            layout.add(view);

        addView(layout);
    }




    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        int width = MeasureSpec.getSize(wSpec);
        int height=MeasureSpec.getSize(hSpec);
        wSpec  = MeasureSpec.makeMeasureSpec (width,  MeasureSpec.EXACTLY);
        hSpec  = MeasureSpec.makeMeasureSpec (height,  MeasureSpec.EXACTLY);
        setMeasuredDimension(width, height);
        
        layout.measure(wSpec, hSpec);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layout.layout(0, 0, r-l, b-t);
    }
}
