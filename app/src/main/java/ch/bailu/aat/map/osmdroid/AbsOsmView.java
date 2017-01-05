package ch.bailu.aat.map.osmdroid;

import android.content.Context;
import android.view.ViewGroup;

import org.osmdroid.util.BoundingBoxOsm;
import org.osmdroid.views.MapView;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.map.osmdroid.overlay.OsmOverlay;
import ch.bailu.aat.map.osmdroid.overlay.OverlayList;
import ch.bailu.aat.map.osmdroid.overlay.gpx.IconCache;


public abstract class AbsOsmView extends ViewGroup {
    public final MapView map;
    public final MapDensity res;
    public final IconCache mapIconCache = new IconCache();


    private BoundingBoxE6 pendingFrameBounding=null;

    private final OverlayList overlayList;




    public AbsOsmView(Context context, AbsTileProvider provider, MapDensity r) {
        super(context);

        res = r;
        overlayList = new OverlayList(this);

        map = new MapView(context, r.getTileSize(), provider);
        map.getOverlayManager().add(overlayList);
        addView(map);
    }


    public OsmOverlay add(OsmOverlay o) {
        return overlayList.add(o);
    }

    public void add(OsmOverlay[] overlays) {
        for (OsmOverlay overlay: overlays) {
            overlayList.add(overlay);
        }
    }


    public void requestRedraw() {
        map.invalidate();
    }



    public void frameBoundingBox(BoundingBoxE6 boundingBox)  {
        if (this.getWidth()==0 || this.getHeight()==0) {
            pendingFrameBounding=boundingBox;
        } else {
            BoundingBoxOsm bounding = boundingBox.toBoundingBoxE6();
            frameBoundingE6(bounding);
        }
    }


    private void frameBoundingE6(BoundingBoxOsm bounding) {
        if (bounding.getDiagonalLengthInMeters()<5) {
            map.getController().setZoom(14);
        } else {
            map.getController().zoomToSpan(bounding);
        }
        map.getController().setCenter(bounding.getCenter());
        pendingFrameBounding=null;
    }

    public abstract String getSolidKey();

    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        // As big as possible
        wSpec  = MeasureSpec.makeMeasureSpec (MeasureSpec.getSize(wSpec),  MeasureSpec.EXACTLY);
        hSpec  = MeasureSpec.makeMeasureSpec (MeasureSpec.getSize(hSpec),  MeasureSpec.EXACTLY);

        map.measure(wSpec, hSpec);
        setMeasuredDimension(map.getMeasuredWidth(), map.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (changed) {
            map.layout(0, 0, r - l, b - t);
            if (pendingFrameBounding != null)
                frameBoundingBox(pendingFrameBounding);
        }

        overlayList.onLayout(changed, l, t, r, b);
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (pendingFrameBounding != null) 
            frameBoundingBox(pendingFrameBounding);
    }
    

    public OverlayList getOverlayList() {
        return overlayList;
    }
    

    
    @Override
    public void onDetachedFromWindow() {
        mapIconCache.close();
        super.onDetachedFromWindow();
    }
}
