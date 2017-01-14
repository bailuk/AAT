//package ch.bailu.aat.map.osmdroid;
//
//import android.content.SharedPreferences;
//import android.view.View;
//import android.view.ViewGroup;
//
//import org.mapsforge.core.model.LatLong;
//import org.osmdroid.util.BoundingBoxOsm;
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.MapView;
//
//import java.util.ArrayList;
//
//import ch.bailu.aat.coordinates.BoundingBoxE6;
//import ch.bailu.aat.map.MapContext;
//import ch.bailu.aat.map.MapDensity;
//import ch.bailu.aat.map.MapViewInterface;
//import ch.bailu.aat.map.layer.MapLayerInterface;
//import ch.bailu.aat.services.ServiceContext;
//
//public class OsmViewAbstract extends ViewGroup implements MapViewInterface, SharedPreferences.OnSharedPreferenceChangeListener {
//
//    public final MapView map;
//    private final ArrayList<MapLayerInterface> overlays = new ArrayList(10);
//
//    private final OsmContext mcontext;
//    private final OsmTileProviderAbstract tileProvider;
//
//    private BoundingBoxE6 pendingFrameBounding=null;
//
//
//
//    public OsmViewAbstract(ServiceContext sc, OsmTileProviderAbstract provider, MapDensity r, String skey) {
//        super(sc.getContext());
//
//        tileProvider = provider;
//        map = new MapView(sc.getContext(), r.getTileSize(), tileProvider);
//        addView(map);
//
//        mcontext = new OsmContext(this, sc, r, skey);
//        map.getOverlayManager().add(mcontext);
//    }
//
//
//
//    @Override
//    public void zoomOut() {
//        map.getController().zoomOut();
//    }
//
//    @Override
//    public void zoomIn() {
//        map.getController().zoomIn();
//    }
//
//    @Override
//    public void requestRedraw() {
//        map.invalidate();
//    }
//
//    @Override
//    public void add(MapLayerInterface l) {
//        map.getOverlayManager().add(new OverlayWrapper(l, mcontext));
//        overlays.add(l);
//
//    }
//
//    @Override
//    public MapContext getMContext() {
//        return mcontext;
//    }
//
//    @Override
//    public void setZoomLevel(byte z) {
//        map.getController().setZoom(z);
//    }
//
//    @Override
//    public void setCenter(LatLong l) {
//        map.getController().setCenter(new GeoPoint(l.getLatitude(), l.getLongitude()));
//    }
//
//    @Override
//    public View toView() {
//        return this;
//    }
//
//    @Override
//    public void reDownloadTiles() {
//        tileProvider.reDownloadTiles();
//    }
//
//    @Override
//    public void close() {
//
//    }
//
//
//    @Override
//    public void frameBounding(BoundingBoxE6 boundingBox) {
//        if (this.getWidth()==0 || this.getHeight()==0) {
//            pendingFrameBounding=boundingBox;
//        } else {
//            BoundingBoxOsm bounding = boundingBox.toBoundingBoxE6();
//            frameBoundingE6(bounding);
//        }
//    }
//
//
//    private void frameBoundingE6(BoundingBoxOsm bounding) {
//        if (bounding.getDiagonalLengthInMeters()<5) {
//            map.getController().setZoom(14);
//        } else {
//            map.getController().zoomToSpan(bounding);
//        }
//        map.getController().setCenter(bounding.getCenter());
//        pendingFrameBounding=null;
//    }
//
//
//    @Override
//    protected void onMeasure(int wSpec, int hSpec) {
//        // As big as possible
//        wSpec  = View.MeasureSpec.makeMeasureSpec (View.MeasureSpec.getSize(wSpec),  View.MeasureSpec.EXACTLY);
//        hSpec  = View.MeasureSpec.makeMeasureSpec (View.MeasureSpec.getSize(hSpec),  View.MeasureSpec.EXACTLY);
//
//        map.measure(wSpec, hSpec);
//        setMeasuredDimension(map.getMeasuredWidth(), map.getMeasuredHeight());
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//
//        if (changed) {
//            map.layout(0, 0, r - l, b - t);
//            if (pendingFrameBounding != null)
//                frameBounding(pendingFrameBounding);
//        }
//
//        for (MapLayerInterface o: overlays) o.onLayout(changed, l, t, r, b);
//    }
//
//
//    @Override
//    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        if (pendingFrameBounding != null)
//            frameBounding(pendingFrameBounding);
//    }
//
//
//
//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        for (MapLayerInterface o: overlays) o.onAttached();
//    }
//
//    @Override
//    public void onDetachedFromWindow() {
//        for (MapLayerInterface o: overlays) o.onDetached();
//        super.onDetachedFromWindow();
//    }
//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences p, String k) {
//        for (MapLayerInterface o: overlays) o.onSharedPreferenceChanged(p,k);
//    }
//}
