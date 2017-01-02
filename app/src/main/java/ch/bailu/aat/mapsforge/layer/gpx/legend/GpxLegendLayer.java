package ch.bailu.aat.mapsforge.layer.gpx.legend;

import android.content.SharedPreferences;
import android.graphics.Color;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.mapsforge.layer.MapsForgeLayer;
import ch.bailu.aat.mapsforge.layer.context.MapContext;
import ch.bailu.aat.mapsforge.layer.context.MapContextTwoNodes;
import ch.bailu.aat.mapsforge.layer.gpx.GpxLayer;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.MapTwoNodes;
import ch.bailu.aat.views.map.overlay.gpx.legend.*;

public class GpxLegendLayer extends GpxLayer {
    private final LegendWalker walker;
    private final MapContext mcontext;

    public GpxLegendLayer(MapContext c, LegendWalker w) {
        super(Color.DKGRAY);
        mcontext = c;
        walker=w;
    }

    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {

        // TODO move to constructor
        LegendContext context = new LegendContext(
                mcontext,
                new MapContextTwoNodes(mcontext.metrics));

        walker.init(context);
        walker.walkTrack(getGpxList());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

}
