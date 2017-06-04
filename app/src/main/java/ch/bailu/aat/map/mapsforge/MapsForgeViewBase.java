package ch.bailu.aat.map.mapsforge;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.controller.FrameBufferController;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.view.FrameBuffer;

import java.util.ArrayList;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppLog;

public class MapsForgeViewBase extends MapView implements
        MapViewInterface,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private BoundingBox pendingFrameBounding=null;

    private boolean areLayersAttached=false, areServicesUp=false, isVisible=true;


    private final MapsForgeContext mcontext;
    private final Storage storage;


    private final FrameBufferHack frameBufferHack;
    private final FrameBufferController frameBufferControllerHack;

    private final ArrayList<MapLayerInterface> layers = new ArrayList(10);



    public MapsForgeViewBase(ServiceContext sc, String key, MapDensity d) {
        super(sc.getContext());
        getModel().displayModel.setFixedTileSize(d.getTileSize());

        mcontext = new MapsForgeContext(this, sc, key, d);
        add(mcontext, mcontext);

        storage = Storage.global(mcontext.getContext());

        getMapScaleBar().setVisible(false);
        setBuiltInZoomControls(false);

        this.setGestureDetector(new GestureDetector(getContext(), new SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                for(MapLayerInterface layer: layers) layer.onTap(new Point(e.getX(), e.getY()));
                return false;
            }
        }));

        FrameBufferController.setUseSquareFrameBuffer(false);
        frameBufferHack = new FrameBufferHack(getModel());
        frameBufferControllerHack = FrameBufferController.create(this.frameBufferHack, getModel());
    }




    @Override
    public FrameBuffer getFrameBuffer() {
        if (frameBufferHack == null) return super.getFrameBuffer();

        super.getFrameBuffer().destroy();
        return frameBufferHack;
    }

    @Override
    protected void onDraw(Canvas androidCanvas) {
        org.mapsforge.core.graphics.Canvas gc = AndroidGraphicFactory.createGraphicContext(androidCanvas);
        frameBufferHack.draw(gc);
        gc.destroy();

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
    public MapViewPosition getMapViewPosition() {
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
        getLayerManager().redrawLayers();
    }


/*    @Override
    public void repaint() {
        postInvalidateDelayed(15);
    }
*/

    @Override
    public void frameBounding(BoundingBoxE6 boundingBox) {
        if (boundingBox.hasBounding())
            frameBounding(boundingBox.toBoundingBox());
    }


    public void frameBounding(BoundingBox bounding) {
        Dimension dimension = getModel().mapViewDimension.getDimension();


        if (dimension == null) {
            pendingFrameBounding=bounding;
        } else {
            byte zoom = LatLongUtils.zoomForBounds(
                    dimension,
                    bounding,
                    getModel().displayModel.getTileSize());

            zoom = (byte) Math.min(zoom, getModel().mapViewPosition.getZoomLevelMax());
            zoom = (byte) Math.max(zoom, getModel().mapViewPosition.getZoomLevelMin());

            MapPosition position = new MapPosition(bounding.getCenterPoint(), zoom);
            getModel().mapViewPosition.setMapPosition(position);

            pendingFrameBounding=null;
        }
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
        for (MapLayerInterface layer: layers) layer.onLayout(c,l,t,r,b);
    }

    public ArrayList<MapLayerInterface> getLayers() {
        return layers;
    }

    @Override
    public void setVisibility(int v) {
        super.setVisibility(v);
        changeVisibility(v);
    }

    private void changeVisibility(int v) {

        isVisible = (v == VISIBLE);

        if (isVisible)
            AppLog.d(this, "is visible");
        else AppLog.d(this, "is NOT visible");

        if (isShown())
            AppLog.d(this, "is shown");
        else AppLog.d(this, "is NOT shown");

        attachDetachLayers();
    }


    protected void enableLayers() {
        isVisible = true;
        areServicesUp = true;
        attachDetachLayers();
    }

    private void disableLayers() {
        isVisible = false;
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

    private void attachDetachLayers() {
        if (areLayersAttached != (isVisible && areServicesUp)) {
            areLayersAttached = (isVisible && areServicesUp);

            if (areLayersAttached) {
                for (MapLayerInterface l : layers) l.onAttached();
            } else {
                for (MapLayerInterface l : layers) l.onDetached();
            }
        }
    }


    @Override
    public void onDestroy() {
        disableLayers();
        destroyAll();
        frameBufferControllerHack.destroy();
        frameBufferHack.destroy();
    }
}