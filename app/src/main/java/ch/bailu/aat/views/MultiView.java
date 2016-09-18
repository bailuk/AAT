package ch.bailu.aat.views;

import android.content.Context;
import android.util.SparseArray;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.preferences.Storage;


public class MultiView extends TrackDescriptionView {

    private final SparseArray<GpxInformation> informationMap =
            new SparseArray<>(5);
    
    private final TrackDescriptionView[] viewList;
    private int active=0;
    
    public MultiView(Context context, String key, int filter, TrackDescriptionView[] d) {
        super(context, key, filter);

        viewList = d;

        for (TrackDescriptionView aData : viewList) {
            aData.setVisibility(GONE);
            addView(aData);
        }
        
        setActive(Storage.activity(context).readInteger(solidKey + "_index"));
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
            viewList[active].setVisibility(GONE);
        }

        active=a;
        if (active >= viewList.length) active=0;
        else if (active < 0) active= viewList.length-1;
        
        
        viewList[active].setVisibility(VISIBLE);
        viewList[active].bringToFront();
        
        for (int i=0; i< informationMap.size(); i++) 
            viewList[active].updateGpxContent(informationMap.valueAt(i));
    }
    
    
    @Override
    public void updateGpxContent(GpxInformation info) {
        if (filter.pass(info)) {
            informationMap.put(info.getID(), info);
            for (int i = 0; i< viewList.length; i++) viewList[i].updateGpxContent(info);
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
        for (TrackDescriptionView view : viewList) {
            view.measure(wSpec, hSpec);
        }
        setMeasuredDimension(width, height);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (TrackDescriptionView view : viewList) {
            view.layout(0, 0, r-l, b-t);
        }
    }
    
    @Override
    public void onDetachedFromWindow() {
        Storage.activity(getContext()).writeInteger(solidKey + "_index",active);
        super.onDetachedFromWindow();
    }

}
