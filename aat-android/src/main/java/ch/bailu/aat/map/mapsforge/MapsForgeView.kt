package ch.bailu.aat.map.mapsforge;

import android.content.Context;
import android.graphics.Canvas;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.model.common.Observer;

import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat_lib.map.tile.MapsForgeTileLayerStackConfigured;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.map.layer.MapPositionLayer;
import ch.bailu.aat_lib.service.ServicesInterface;

public class MapsForgeView extends MapsForgeViewBase {

    private final MapsForgeForeground foreground;
    private final MapsForgeTileLayerStackConfigured stack;
    private final MapPositionLayer pos;
    private final ServicesInterface services;

    public MapsForgeView(Context context, AppContext appContext, DispatcherInterface dispatcher, String key) {
        super(appContext, context, key, new MapDensity(context));

        stack = new MapsForgeTileLayerStackConfigured.All(this, appContext);
        add(stack, stack);

        services = appContext.getServices();

        // Depends on zoom limits (setItem by TileLayerStack)
        pos = new MapPositionLayer(getMContext(), new Storage(context), dispatcher);
        add(pos);

        foreground = new MapsForgeForeground(appContext,this,
                getMContext(),
                new MapDensity(context),
                getLayers());

        setClickable(true);

        getModel().mapViewPosition.addObserver(new Observer() {
            private LatLong center = getModel().mapViewPosition.getCenter();

            @Override
            public void onChange() {
                LatLong newCenter = getModel().mapViewPosition.getCenter();

                if (newCenter != null && !newCenter.equals(center)) {
                    center = newCenter;
                    pos.onMapCenterChanged(center);
                }
            }
        });

    }


    @Override
    public void reDownloadTiles() {
        stack.reDownloadTiles();
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        foreground.dispatchDraw(services, canvas);
    }
}
