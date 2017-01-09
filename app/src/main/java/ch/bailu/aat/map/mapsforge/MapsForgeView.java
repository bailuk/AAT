package ch.bailu.aat.map.mapsforge;

import android.content.SharedPreferences;
import android.view.View;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;

import java.util.ArrayList;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.map.Attachable;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.map.layer.MapPositionLayer;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppLog;

public class MapsForgeView extends MapView implements
        MapViewInterface,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private final static byte ZOOM_MAX = 14;
    private final static byte ZOOM_MIN = 4;

    private BoundingBox pendingFrameBounding=null;

    private boolean attached=false;
    private final MapsForgeContext mcontext;
    private final Storage storage;


    private final ArrayList<MapLayerInterface> layers = new ArrayList(10);


    public MapsForgeView(ServiceContext sc, DispatcherInterface dispatcher, String key) {
        super(sc.getContext());
        mcontext = new MapsForgeContext(this, sc, key);
        add(mcontext, mcontext);

        MapPositionLayer pos = new MapPositionLayer(mcontext, dispatcher);
        add(pos);

        storage = Storage.global(mcontext.getContext());

        MapsForgeTileLayer tiles = new MapsForgeTileLayer(
                mcontext.getSContext(),
                getModel().mapViewPosition,
                AndroidGraphicFactory.INSTANCE.createMatrix());

         add(tiles, tiles);

        setClickable(true);
        getMapScaleBar().setVisible(false);
        setBuiltInZoomControls(false);

        setZoomLevelMax(ZOOM_MAX);
        setZoomLevelMin(ZOOM_MIN);
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
    public void reDownloadTiles() {

    }

    @Override
    public void close() {
        AppLog.d(this, "destroyAll()");
        destroyAll();
    }

    public void add(Layer mfLayer, MapLayerInterface layer) {
        this.addLayer(mfLayer);
        layers.add(layer);
        if (attached) layer.onAttached();

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


    @Override
    public void frameBounding(BoundingBoxE6 boundingBox) {
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

            if (zoom > ZOOM_MAX) zoom = ZOOM_MAX;
            if (zoom < ZOOM_MIN) zoom = ZOOM_MIN;

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
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        attached = true;
        storage.register(this);
        for (Attachable layer: layers) layer.onAttached();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attached = false;

        storage.unregister(this);

        for (Attachable layer: layers) layer.onDetached();
    }


    @Override
    public void onLayout(boolean c, int l, int t, int r, int b) {
        for (MapLayerInterface layer: layers) layer.onLayout(c,l,t,r,b);
    }


}
