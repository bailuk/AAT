package ch.bailu.aat_lib.map.layer.grid;

import com.google.openlocationcode.OpenLocationCode;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public final class PlusCodesCenterCoordinatesLayer  implements MapLayerInterface {

    private final ElevationLayer elevation;
    private final Crosshair crosshair;

    public PlusCodesCenterCoordinatesLayer (ServicesInterface services, StorageInterface storageInterface) {
        elevation = new ElevationLayer(services, storageInterface);
        crosshair = new Crosshair();
    }
    @Override
    public void drawForeground(MapContext mcontext) {
        final LatLong point = mcontext.getMapView().getMapViewPosition().getCenter();

        crosshair.drawForeground(mcontext);
        drawCoordinates(mcontext, point);
        elevation.drawForeground(mcontext);
    }


    @Override
    public void drawInside(MapContext mcontext) {


    }

    @Override
    public boolean onTap( Point tapXY) {
        return false;
    }


    private void drawCoordinates(MapContext clayer,LatLong point) {
        final OpenLocationCode center =
                new OpenLocationCode(point.latitude, point.longitude);

        final String code = center.getCode();
        clayer.draw().textBottom(code,1);

    }


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}


    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}


    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
