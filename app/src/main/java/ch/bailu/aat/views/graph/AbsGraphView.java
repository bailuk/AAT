package ch.bailu.aat.views.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.view.ViewGroup;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.preferences.SolidUnit;
import ch.bailu.aat.util.ui.AppLog;

public abstract class AbsGraphView extends ViewGroup implements OnContentUpdatedInterface {
    
    public final static int SAMPLE_WIDTH_PIXEL=5;
    
    private final SolidUnit sunit;
    private final StringBuilder builder = new StringBuilder();
    
    private boolean markerMode=false;
    
    private GpxList gpxCache = GpxList.NULL_TRACK;
    
    public AbsGraphView(Context context, DispatcherInterface di, int iid) {
        super(context);
        di.addTarget(this, iid);
        setWillNotDraw(false);
        sunit = new SolidUnit(context);
    }

    
   

    
    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        gpxCache = info.getGpxList();
        invalidate();
    }
    
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        AppLog.d(this, "onLayout()");
    }

    
    @Override
    public void onDraw(Canvas c) {
        if (getWidth() > 0 && getHeight() > 0) {
            AppLog.d(this, "onDraw()");
            markerMode = gpxCache.getMarkerList().size() > getWidth() / SAMPLE_WIDTH_PIXEL;
            plot(c, gpxCache, sunit, markerMode);
        }
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
