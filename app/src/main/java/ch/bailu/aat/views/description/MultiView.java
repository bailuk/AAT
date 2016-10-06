package ch.bailu.aat.views.description;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;

import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.views.description.TrackDescriptionView;


public class MultiView extends TrackDescriptionView {


    private final SparseArray<GpxInformation> informationMap =
            new SparseArray<>(5);
    
    private final DescriptionInterface[] targets;
    private final View[] views;
    private int active=0;


    public MultiView(Context context, String key, int filter,
                     View[] v,
                     DescriptionInterface[]t) {
        super(context, key, filter);

        views = v;
        targets = t;

        for (View view : views) {
            view.setVisibility(GONE);
            addView(view);
        }

        setActive(Storage.activity(context).readInteger(solidKey + "_index"));

    }
    public MultiView(Context context, String key, int filter, TrackDescriptionView[] d) {
        this(context, key, filter, d, d);
    }

    public MultiView(Context context, String key, int filter, View[] v) {
        this(context, key, filter, v, createNullDescription(v.length));
    }

    private static DescriptionInterface[] createNullDescription(int l) {
        DescriptionInterface[] r = new DescriptionInterface[l];

        for (int i = 0; i<r.length; i++) {
            r[i] = DescriptionInterface.NULL;
        }
        return r;
    }


    public void setNext() {
        setActive(active+1);
    }
    
    public void setPrevious() {
        setActive(active-1);
    }


    public int getActive() {
        return active;
    }

    public void setActive(int a) {
        if (a != active) {
            views[active].setVisibility(GONE);
        }

        active=a;
        if (active >= views.length) active=0;
        else if (active < 0) active= views.length-1;
        
        
        views[active].setVisibility(VISIBLE);
        views[active].bringToFront();
        
        for (int i=0; i< informationMap.size(); i++) 
            targets[active].updateGpxContent(informationMap.valueAt(i));
    }
    
    
    @Override
    public void updateGpxContent(GpxInformation info) {
        if (filter.pass(info)) {
            informationMap.put(info.getID(), info);
            targets[active].updateGpxContent(info);
        }
    }

    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        int width = MeasureSpec.getSize(wSpec);
        int height = MeasureSpec.getSize(hSpec);


        // As big as possible
        wSpec  = MeasureSpec.makeMeasureSpec (width,  MeasureSpec.EXACTLY);
        hSpec  = MeasureSpec.makeMeasureSpec (height,  MeasureSpec.EXACTLY);

        //int width=0,height=0;
        for (View view : views) {
            view.measure(wSpec, hSpec);
        }
        setMeasuredDimension(width, height);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (View view : views) {
            view.layout(0, 0, r-l, b-t);
        }
    }
    
    @Override
    public void onDetachedFromWindow() {
        Storage.activity(getContext()).writeInteger(solidKey + "_index",active);
        super.onDetachedFromWindow();
    }

}
