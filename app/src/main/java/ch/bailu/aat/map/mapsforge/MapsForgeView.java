package ch.bailu.aat.map.mapsforge;

import android.content.SharedPreferences;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;
import ch.bailu.aat.map.mapsforge.layer.MapPositionLayer;
import ch.bailu.aat.map.mapsforge.layer.MapsForgeLayer;
import ch.bailu.aat.map.mapsforge.layer.MapsForgeLayerInterface;
import ch.bailu.aat.map.mapsforge.util.Attachable;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeView extends MapView implements
        SharedPreferences.OnSharedPreferenceChangeListener {


    private boolean attached=false;
    public final MapContext mcontext;
    private final Storage storage;


    private final ArrayList<MapsForgeLayerInterface> layers = new ArrayList(10);


    public MapsForgeView(ServiceContext sc, DispatcherInterface dispatcher, String key) {
        super(sc.getContext());
        mcontext = new MapContext(this, sc, key);
        add(mcontext);

        MapPositionLayer pos = new MapPositionLayer(mcontext, dispatcher);
        add(pos);

        storage = Storage.global(mcontext.context);
/*
        MapsForgeTileLayer tiles = new MapsForgeTileLayer(
                mcontext.scontext,
                getModel().mapViewPosition,
                AndroidGraphicFactory.INSTANCE.createMatrix());

         add(tiles, tiles);
*/

        TileCache tileCache = AndroidUtil.createTileCache(
                sc.getContext(),
                "mapcache",
                getModel().displayModel.getTileSize(),
                1f,
                getModel().frameBufferModel.getOverdrawFactor());


        MapDataStore mapDataStore = new MapFile(new File("/storage/emulated/0/switzerland.map"));
        TileRendererLayer tiles = new TileRendererLayer(tileCache,
                mapDataStore,
                getModel().mapViewPosition,
                AndroidGraphicFactory.INSTANCE);

        tiles.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
        addLayer(tiles);




        setClickable(true);
        getMapScaleBar().setVisible(false);
        setBuiltInZoomControls(false);
    }





    public void add(Layer layer, MapsForgeLayerInterface attachable) {
        this.addLayer(layer);
        layers.add(attachable);
        if (attached) attachable.onAttached();
    }


    public void add(MapsForgeLayer layer) {
        add(layer, layer);
    }


    public void frameBounding(BoundingBoxE6 boundingBox) {
        frameBounding(boundingBox.toBoundingBox());
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
        for (MapsForgeLayerInterface layer: layers) layer.onLayout(c,l,t,r,b);
    }


}
