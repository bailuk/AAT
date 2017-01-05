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
import ch.bailu.aat.map.mapsforge.context.MapsForgeContext;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeView extends MapView implements
        MapViewInterface,
        SharedPreferences.OnSharedPreferenceChangeListener {


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

    public void add(Layer mfLayer, MapLayerInterface layer) {
        this.addLayer(mfLayer);
        layers.add(layer);
        if (attached) layer.onAttached();
    }




    public void frameBounding(BoundingBoxE6 boundingBox) {
        frameBounding(boundingBox.toBoundingBox());
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

    public void frameBounding(BoundingBox bounding) {
        Dimension dimension = getModel().mapViewDimension.getDimension();
        byte zoom = LatLongUtils.zoomForBounds(
                dimension,
                bounding,
                getModel().displayModel.getTileSize());

        MapPosition position = new MapPosition(bounding.getCenterPoint(), zoom);

        getModel().mapViewPosition.setMapPosition(position);
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
        super.onLayout(c,l,t,r,b);
        for (MapLayerInterface layer: layers) layer.onLayout(c,l,t,r,b);
    }


}
