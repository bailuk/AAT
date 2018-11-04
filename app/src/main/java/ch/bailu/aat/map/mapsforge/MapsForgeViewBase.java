package ch.bailu.aat.map.mapsforge;

import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.model.IMapViewPosition;

import java.util.ArrayList;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeViewBase extends MapView implements
        MapViewInterface,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private BoundingBox pendingFrameBounding=null;

    private final MapsForgeContext mcontext;
    private final Storage storage;

    public boolean areServicesUp=false;
    public boolean isVisible=false;


    private final ArrayList<MapLayerInterface> layers = new ArrayList<>(10);

    private boolean areLayersAttached=false;


    public MapsForgeViewBase(ServiceContext sc, String key, MapDensity d) {
        super(sc.getContext());

        this.setBackgroundColor(getModel().displayModel.getBackgroundColor());
        getModel().displayModel.setFixedTileSize(d.getTileSize());

        mcontext = new MapsForgeContext(this, sc, key, d);
        add(mcontext, mcontext);

        storage = new Storage(mcontext.getContext());

        getMapScaleBar().setVisible(false);
        setBuiltInZoomControls(false);

        this.setGestureDetector(new GestureDetector(getContext(), new SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                for(MapLayerInterface layer: layers) layer.onTap(new Point(e.getX(), e.getY()));
                return false;
            }
        }));


    }

    @Override
    public void onChange() {
        // Disable MapView.onChange to fix a speed bug in MapsForge
    }


    public void add(MapLayerInterface layer) {
        LayerWrapper wrapper = new LayerWrapper(mcontext, layer);
        add(wrapper, layer);
    }

    @Override
    public MapContext getMContext() {
        return mcontext;
    }

    @Override
    public View toView() {
        return this;
    }

    @Override
    public void reDownloadTiles() {}

    @Override
    public IMapViewPosition getMapViewPosition() {
        return getModel().mapViewPosition;
    }


    public void add(Layer mfLayer, MapLayerInterface layer) {
        addLayer(mfLayer);
        layers.add(layer);
        if (areLayersAttached) layer.onAttached();

    }


    @Override
    public void zoomOut() {
        getModel().mapViewPosition.zoomOut();
    }

    @Override
    public void zoomIn() {
        getModel().mapViewPosition.zoomIn();
    }

    @Override
    public void requestRedraw() {
        if (areLayersAttached)
            getLayerManager().redrawLayers();
    }


    @Override
    public void frameBounding(BoundingBoxE6 boundingBox) {
        frameBounding(boundingBox.toBoundingBox());
    }


    private void frameBounding(BoundingBox bounding) {
        Dimension dimension = getModel().mapViewDimension.getDimension();


        if (dimension == null) {
            pendingFrameBounding=bounding;
        } else {
            byte zoom = zoomForBounds(bounding, dimension);

            MapPosition position = new MapPosition(bounding.getCenterPoint(), zoom);
            getModel().mapViewPosition.setMapPosition(position);

            pendingFrameBounding=null;
        }
    }


    private byte zoomForBounds(BoundingBox bounding, Dimension dimension) {
        byte zoom;
        if (bounding.minLatitude == 0d && bounding.minLongitude == 0d
                && bounding.maxLatitude == 0d && bounding.maxLongitude == 0d) {
            zoom = 0;
        } else {
            zoom = LatLongUtils.zoomForBounds(
                    dimension,
                    bounding,
                    getModel().displayModel.getTileSize());
        }

        zoom = (byte) Math.min(zoom, getModel().mapViewPosition.getZoomLevelMax());
        zoom = (byte) Math.max(zoom, getModel().mapViewPosition.getZoomLevelMin());

        return zoom;
    }


    @Override
    public void onSizeChanged(int nw, int nh, int ow, int oh) {
        super.onSizeChanged(nw, nh, ow, oh);

        if (pendingFrameBounding != null) {
            frameBounding(pendingFrameBounding);
        }
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences p, String key) {
        for(SharedPreferences.OnSharedPreferenceChangeListener l: layers)
            l.onSharedPreferenceChanged(p, key);
    }


    @Override
    public void onLayout(boolean c, int l, int t, int r, int b) {

        if (c) {
            for (MapLayerInterface layer: layers) layer.onLayout(c,l,t,r,b);
        }
    }

    public ArrayList<MapLayerInterface> getLayers() {
        return layers;
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        isVisible = (getVisibility() == VISIBLE);

        attachDetachLayers();

    }


    @Override
    public void onDetachedFromWindow() {
        isVisible = false;
        attachDetachLayers();

        super.onDetachedFromWindow();

    }


    @Override
    public void setVisibility(int v) {
        super.setVisibility(v);

        isVisible = (v == VISIBLE);
        attachDetachLayers();
    }

    @Override
    public void onResumeWithService() {
        storage.register(this);
        areServicesUp = true;

        attachDetachLayers();
    }

    @Override
    public void onPauseWithService() {
        storage.unregister(this);
        areServicesUp = false;

        attachDetachLayers();
    }



    @Override
    public void onDestroy() {
        detachLayers();
        destroyAll();
    }


    private void attachDetachLayers() {
        if (isVisible && areServicesUp) {
            attachLayers();
        } else {
            detachLayers();
        }
    }

    protected void attachLayers() {
        if (!areLayersAttached) {
            for (MapLayerInterface l : layers) l.onAttached();
            areLayersAttached = true;
            requestRedraw();
        }
    }


    private void detachLayers() {
        if (areLayersAttached) {
            for (MapLayerInterface l : layers) l.onDetached();
            areLayersAttached = false;
        }
    }
}