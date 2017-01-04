package ch.bailu.aat.map.layer.gpx.legend;

import android.content.SharedPreferences;
import android.graphics.Color;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.TwoNodes;
import ch.bailu.aat.map.layer.gpx.GpxLayer;

public class GpxLegendLayer extends GpxLayer {
    private final LegendWalker walker;
    private final MapContext mcontext;

    public GpxLegendLayer(MapContext c, LegendWalker w) {
        super(Color.DKGRAY);
        mcontext = c;
        walker=w;
    }

    @Override
    public void draw(MapContext mcontext) {

        // TODO move to constructor
        LegendContext context = new LegendContext(
                mcontext,
                new TwoNodes(mcontext.getMetrics()));

        walker.init(context);
        walker.walkTrack(getGpxList());
    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
