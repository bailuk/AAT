package ch.bailu.aat.views;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.gpx.GpxInformation;


import android.content.Context;

public abstract class DescriptionViewGroup extends TrackDescriptionView {

    private ContentDescription[] data;
    private TrackDescriptionView[] view;

    
    public DescriptionViewGroup(Context context, String key, int filter) {
        super(context,key, filter);
    }
    
    public void init(ContentDescription[]d, TrackDescriptionView[] v) {
        data=d; view=v;
    }
    
    public void init(TrackDescriptionView[] v) {
        view=v;
    }
    
    protected int getDescriptionCount() {
        return view.length;
    }
    
    protected ContentDescription getDescription(int index) {
        return data[index];
    }
    
    protected TrackDescriptionView getDescriptionView(int index) {
        return view[index];
    }
    
    @Override
    protected void onMeasure(int w, int h) {
        for (TrackDescriptionView child: view) {
            child.measure(w, h);
        }
        setMeasuredDimension(w,h);
    }
    
    
    @Override
    public void updateGpxContent(GpxInformation info) {
        if (filter.pass(info)) {
            for (int i=0; i<view.length; i++) view[i].updateGpxContent(info);
        }   
    }


}
