package ch.bailu.aat.views;

import android.content.Context;
import android.util.SparseArray;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.preferences.Storage;


public class MultiView extends TrackDescriptionView {

    private  SparseArray<GpxInformation> informationMap = 
        new SparseArray<GpxInformation>(5);
    
    private TrackDescriptionView[] data;
    private int active=0;
    
    public MultiView(Context context, String key, int filter, TrackDescriptionView[] d) {
        super(context, key, filter);

        data = d;
        
        for (int i=0; i<data.length; i++) {
            data[i].setVisibility(GONE);
            addView(data[i]);
        }
        
        setActive(Storage.activity(context).readInteger(solidKey + "_index"));
    }

    public void setNext() {
        setActive(active+1);
    }
    
    public void setPrevious() {
        setActive(active-1);
    }

    private void setActive(int a) {
        data[active].setVisibility(GONE);
        
        active=a;
        if (active >= data.length) active=0;
        else if (active < 0) active=data.length-1;
        
        
        data[active].setVisibility(VISIBLE);
        data[active].bringToFront();
        
        for (int i=0; i< informationMap.size(); i++) 
            data[active].updateGpxContent(informationMap.valueAt(i));
    }
    
    
    @Override
    public void updateGpxContent(GpxInformation info) {
        if (filter.pass(info)) {
            informationMap.put(info.getID(), info);
            for (int i=0; i<data.length; i++) data[i].updateGpxContent(info);
        }
    }

    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        // As big as possible
        wSpec  = MeasureSpec.makeMeasureSpec (MeasureSpec.getSize(wSpec),  MeasureSpec.EXACTLY);
        hSpec  = MeasureSpec.makeMeasureSpec (MeasureSpec.getSize(hSpec),  MeasureSpec.EXACTLY);

        //int width=0,height=0;
        for (int i=0; i<data.length; i++) {
            data[i].measure(wSpec,hSpec);
        }
        setMeasuredDimension(wSpec, hSpec);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i=0; i<data.length; i++) {
            data[i].layout(0, 0, r-l, b-t);
        }
    }
    
    @Override
    public void onDetachedFromWindow() {
        Storage.activity(getContext()).writeInteger(solidKey + "_index",active);
        super.onDetachedFromWindow();
    };

}
