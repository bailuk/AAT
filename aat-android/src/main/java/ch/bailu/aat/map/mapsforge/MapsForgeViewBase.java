package ch.bailu.aat.map.mapsforge;


import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.model.IMapViewPosition;

import java.util.ArrayList;

import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.dispatcher.LifeCycleInterface;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat_lib.map.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public class MapsForgeViewBase extends MapView implements
        MapViewInterface,
        LifeCycleInterface,
        OnPreferencesChanged {

    private BoundingBox pendingFrameBounding=null;

    private final MapsForgeContext mcontext;
    private final ServicesInterface services;
    private final Storage storage;

    public boolean areServicesUp=false;
    public boolean isVisible=false;



    private final ArrayList<MapLayerInterface> layers = new ArrayList<>(10);

    private boolean areLayersAttached=false;





    public MapsForgeViewBase(Context context, ServicesInterface servicesInterface, String key, MapDensity d) {
        super(context);

        this.setBackgroundColor(getModel().displayModel.getBackgroundColor());
        getModel().displayModel.setFixedTileSize(d.getTileSize());

        services = servicesInterface;
        mcontext = new MapsForgeContext(this, key, d);
        add(mcontext, mcontext);

        storage = new Storage(context);

        getMapScaleBar().setVisible(false);
        setBuiltInZoomControls(false);

        this.setGestureDetector(new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
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


    @Override
    public void add(MapLayerInterface layer) {
        LayerWrapper wrapper = new LayerWrapper(services, mcontext, layer);
        add(wrapper, layer);
    }

    @Override
    public MapContext getMContext() {
        return mcontext;
    }

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
    public void onPreferencesChanged(StorageInterface s, String key) {
        for(OnPreferencesChanged l: layers)
            l.onPreferencesChanged(s, key);
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

        /* FIXME: this is a workaround to a bug:
         * Sometimes the LayerManager thread is still running after calling destroyAll().
         * This happens when MapView was never attached to window.
         * Same problem with the Animator thread of MapViewPosition. */
        getLayerManager().finish();
        getMapViewPosition().destroy();
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