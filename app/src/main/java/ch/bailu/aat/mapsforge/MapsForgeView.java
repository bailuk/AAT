package ch.bailu.aat.mapsforge;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;

import java.util.ArrayList;

import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.mapsforge.layer.ContextLayer;
import ch.bailu.aat.mapsforge.layer.MapsForgeLayer;
import ch.bailu.aat.mapsforge.layer.MapsForgeLayerInterface;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeView extends MapView implements OnContentUpdatedInterface {
    private final String solid_key;
    private boolean attached=false;

    private final ContextLayer clayer;
    private final ServiceContext scontext;

    private final ArrayList<MapsForgeLayerInterface> layers = new ArrayList(10);


    public MapsForgeView(ServiceContext sc, String key) {
        super(sc.getContext());

        solid_key = key;

        scontext = sc;
        clayer = new ContextLayer(this);
        add(clayer);


        MapsForgeTileLayer tiles = new MapsForgeTileLayer(scontext, getModel().mapViewPosition,
                AndroidGraphicFactory.INSTANCE.createMatrix());

        add(tiles, tiles);
    }



    public ContextLayer getContextLayer() {
        return clayer;
    }


    public void add(Layer layer, MapsForgeLayerInterface attachable) {
        this.addLayer(layer);
        layers.add(attachable);
        if (attached) attachable.onAttached();
    }


    public void add(MapsForgeLayer layer) {
        add(layer, layer);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        attached = true;
        for (Attachable layer: layers) layer.onAttached();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attached = false;
        for (Attachable layer: layers) layer.onDetached();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        for (OnContentUpdatedInterface layer: layers) layer.onContentUpdated(iid, info);
    }

    @Override
    public void onLayout(boolean c, int l, int t, int r, int b) {
        super.onLayout(c,l,t,r,b);
        for (MapsForgeLayerInterface layer: layers) layer.onLayout(c,l,t,r,b);
    }

    public String getSolidKey() {
        return solid_key;
    }

    public ServiceContext getServiceContext() {
        return scontext;
    }

    public void requestRedraw() {
    }
}
