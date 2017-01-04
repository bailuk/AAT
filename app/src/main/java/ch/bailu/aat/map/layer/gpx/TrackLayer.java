package ch.bailu.aat.map.layer.gpx;

import android.content.SharedPreferences;
import android.graphics.Color;

import org.mapsforge.core.graphics.Cap;
import org.mapsforge.core.graphics.Join;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.TwoNodes;
import ch.bailu.aat.views.graph.ColorTable;

public class TrackLayer  extends GpxLayer{
    private static final int STROKE_WIDTH=3;
    private final Paint paint;


    private final MapContext mcontext;

    public TrackLayer(MapContext mc) {
        super(Color.BLACK);
        mcontext = mc;
        paint = AndroidGraphicFactory.INSTANCE.createPaint();

        paint.setStrokeWidth(mcontext.getMetrics().getDensity().toDPf(STROKE_WIDTH));
        paint.setStrokeCap(Cap.ROUND);
        paint.setStrokeJoin(Join.ROUND);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void draw(MapContext mcontext) {
        new TrackPainter().walkTrack(getGpxList());
    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        return false;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }


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
            int altitude=node.point.getAltitude();
            int color= ColorTable.altitude.getColor(altitude);
            paint.setColor(color);
        }
    }
}
