package ch.bailu.aat.map.mapsforge;

import android.content.SharedPreferences;
import android.view.View;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;

import java.util.ArrayList;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.map.Attachable;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.map.layer.MapPositionLayer;
import ch.bailu.aat.map.tile.TileProvider;
import ch.bailu.aat.preferences.SolidTileSize;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.BitmapTileObject;
import ch.bailu.aat.util.ui.AppLog;

public class MapsForgeView extends MapsForgeViewBase {

    private final MapsForgeOnTopView overmap;


    public MapsForgeView(ServiceContext sc, DispatcherInterface dispatcher, String key) {
        super(sc, key, new MapDensity(sc.getContext()));

        MapPositionLayer pos = new MapPositionLayer(getMContext(), dispatcher);
        add(pos);

        MapsForgeTileLayerStack stack = new MapsForgeTileLayerStack(this);
        add(stack);

        setClickable(true);


        overmap = new MapsForgeOnTopView(this,
                getMContext(),
                new MapDensity(sc.getContext()),
                getLayers());

        addView(overmap);
    }


    @Override
    public void onLayout(boolean c, int l, int t, int r, int b) {
        overmap.layout(0,0,r-l, b-t);

        super.onLayout(c,l,t,r,b);
    }
}
