package ch.bailu.aat.views.graph;

import android.content.Context;
import android.graphics.Canvas;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.preferences.SolidUnit;
import ch.bailu.aat.views.TrackDescriptionView;

public abstract class AbsGraphView extends TrackDescriptionView {
    
    public final static int SAMPLE_WIDTH_PIXEL=5;
    
    private SolidUnit sunit;
    private StringBuilder builder = new StringBuilder();
    
    private boolean markerMode=false;
    
    private GpxList gpxList = GpxList.NULL_TRACK;
    
    public AbsGraphView(Context context, String key) {
        super(context, key,INFO_ID_ALL);
        setWillNotDraw(false);
        sunit = new SolidUnit(context);
    }

    
   

    
    @Override
    public void updateGpxContent(GpxInformation info) {
        if (filter.pass(info)) {
            gpxList = info.getGpxList();
            invalidate();
        }
    }
    
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {}

    
    @Override
    public void onDraw(Canvas c) {
        markerMode = gpxList.getMarkerList().size() > getWidth() / SAMPLE_WIDTH_PIXEL;
        plot(c, gpxList, sunit, markerMode);
    }

    
    public String plotterLabel(int id, String unit) {
        builder.setLength(0);
        builder.append(getContext().getString(id)); 
        builder.append(" ["); 
        builder.append(unit);
        builder.append("]");
        if (markerMode) builder.append(".");
        return builder.toString();
    }

    
    public abstract void plot(Canvas canvas, GpxList list, SolidUnit sunit, boolean markerMode);    
    
    
    
}
