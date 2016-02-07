package ch.bailu.aat.views.map;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.views.MapView;

import android.content.Context;
import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.views.TrackDescriptionView;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.OverlayList;
import ch.bailu.aat.views.map.overlay.gpx.MapIconCache;

public abstract class AbsOsmView extends TrackDescriptionView {
    private BoundingBox pendingFrameBounding=null;
    
    public final MapView map;
    public final MapIconCache mapIconCache = new MapIconCache();
    
    private OverlayList overlayList = new OverlayList(this, new OsmOverlay[] {});
    
    
    public AbsOsmView(Context context, String key, AbsTileProvider provider, int tileSize) {
        super(context, key,INFO_ID_ALL);
        
        map = createMapView(provider, tileSize);
        addView(map);
    }

    public AbsOsmView(Context context, AbsTileProvider provider) {
        this(context, DEFAULT_SOLID_KEY, provider, TileObject.TILE_SIZE);
    }
    
    
    private MapView createMapView(AbsTileProvider provider, int tileSize) {
        MapView map = new MapView(
                getContext(),
                tileSize, 
                new DefaultResourceProxyImpl(getContext()),
                provider
                );
        map.getOverlays().add(overlayList);
        return map;
    }
    
    
    

    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        // As big as possible
        wSpec  = MeasureSpec.makeMeasureSpec (MeasureSpec.getSize(wSpec),  MeasureSpec.EXACTLY);
        hSpec  = MeasureSpec.makeMeasureSpec (MeasureSpec.getSize(hSpec),  MeasureSpec.EXACTLY);

        map.measure(wSpec, hSpec);
        setMeasuredDimension(wSpec, hSpec);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        map.layout(0, 0, r-l, b-t);
        
        if (pendingFrameBounding != null) 
            frameBoundingBox(pendingFrameBounding);
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
    public void updateGpxContent(GpxInformation info) {
        if (filter.pass(info)) {
            overlayList.updateGpxContent(info);
        }
    }
    
    
    @Override
    public void onDetachedFromWindow() {
        mapIconCache.cleanUp();
        super.onDetachedFromWindow();
    }

    
    @Override
    public void cleanUp() {
        mapIconCache.cleanUp();
    }
}
