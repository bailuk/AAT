package ch.bailu.aat_lib.map.layer.gpx;

import org.mapsforge.core.graphics.Cap;
import org.mapsforge.core.graphics.Join;
import org.mapsforge.core.graphics.Paint;

import ch.bailu.aat_lib.app.AppGraphicFactory;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.map.TwoNodes;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.util.color.AltitudeColorTable;

public final class TrackLayer  extends GpxLayer {
    private static final int STROKE_WIDTH=3;
    private final Paint paint;

    private final MapContext mcontext;

    public TrackLayer(MapContext mc) {
        mcontext = mc;
        paint = AppGraphicFactory.instance().createPaint();

        paint.setStrokeWidth(mcontext.getMetrics().getDensity().toPixel_f(STROKE_WIDTH));
        paint.setStrokeCap(Cap.ROUND);
        paint.setStrokeJoin(Join.ROUND);
    }

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}

    @Override
    public void drawInside(MapContext mcontext) {
        new TrackPainter().walkTrack(getGpxList());
    }

    @Override
    public boolean onTap(Point tapPos) {
        return false;
    }

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}


    private class TrackPainter extends GpxListPainter {
        public TrackPainter() {
            super(mcontext);

        }


        @Override
        public void drawEdge(TwoNodes nodes) {
            mcontext.draw().edge(nodes, paint);
        }


        @Override
        public void drawNode(TwoNodes.PixelNode node) {
            int altitude= (int) node.point.getAltitude();
            int color= AltitudeColorTable.instance().getColor(altitude);
            paint.setColor(color);
        }
    }
}
