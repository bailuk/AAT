package ch.bailu.aat.views.map;

import android.content.Context;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.views.MapView;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.helpers.AppDensity;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.views.description.TrackDescriptionView;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.OverlayList;
import ch.bailu.aat.views.map.overlay.gpx.MapIconCache;

public abstract class AbsOsmView extends TrackDescriptionView {
    private BoundingBox pendingFrameBounding=null;

    public final MapDensity res;
    public final MapView map;
    public final MapIconCache mapIconCache = new MapIconCache();
    
    private OverlayList overlayList;


    public AbsOsmView(Context context, AbsTileProvider provider, MapDensity density) {
        this(context, DEFAULT_SOLID_KEY, provider, density);
    }

    public AbsOsmView(Context context, String key, AbsTileProvider provider,
                       MapDensity r) {
        super(context, key, InfoID.ALL);

        res = r;
        overlayList = new OverlayList(this, new OsmOverlay[] {});
        map = createMapView(provider);
        addView(map);
    }


    private MapView createMapView(AbsTileProvider provider) {
        MapView map = new MapView(getContext(), res.getTileSize(), provider);
        map.getOverlays().add(overlayList);
        return map;
    }



    
    

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
            map.layout(0, 0, r-l, b-t);
            if (pendingFrameBounding != null)
                frameBoundingBox(pendingFrameBounding);
        }

        overlayList.onLayout(changed, l, t, r, b);
    }
    
    
    public void setOverlayList(OsmOverlay[] overlays) {
        map.getOverlays().remove(overlayList);
        overlayList = new OverlayList(this, overlays);
        map.getOverlays().add(overlayList);
    }
    

    // FIXME: do we need this?
    public long getDrawStartTime() {
        return map.getTileProvider().getStartTime();
    }

    public void frameBoundingBox(BoundingBox boundingBox)  {
        

        if (this.getWidth()==0 || this.getHeight()==0) {
            pendingFrameBounding=boundingBox;
        } else {
            BoundingBoxE6 bounding = boundingBox.toBoundingBoxE6();
            frameBoundingE6(bounding);
        }
    }


    private void frameBoundingE6(BoundingBoxE6 bounding) {
        if (bounding.getDiagonalLengthInMeters()<5) {
            map.getController().setZoom(14);
        } else {
            map.getController().zoomToSpan(bounding);
        }
        map.getController().setCenter(bounding.getCenter());
        pendingFrameBounding=null;
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
    public void onContentUpdated(GpxInformation info) {
        if (filter.pass(info)) {
            overlayList.onContentUpdated(info);
        }
    }
    
    
    @Override
    public void onDetachedFromWindow() {
        mapIconCache.close();
        super.onDetachedFromWindow();
    }

    
}
