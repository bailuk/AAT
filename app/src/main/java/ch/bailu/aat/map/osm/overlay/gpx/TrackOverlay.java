package ch.bailu.aat.map.osm.overlay.gpx;

import android.graphics.Color;
import android.graphics.Paint;

import ch.bailu.aat.views.graph.ColorTable;
import ch.bailu.aat.map.osm.AbsOsmView;
import ch.bailu.aat.map.osm.overlay.MapPainter;
import ch.bailu.aat.map.osm.overlay.MapTwoNodes;
import ch.bailu.aat.map.osm.overlay.MapTwoNodes.PixelNode;

public class TrackOverlay extends GpxOverlay{
    private static final int STROKE_WIDTH=3;
    private final Paint paint=new Paint();



    public TrackOverlay(AbsOsmView osm) {
        super(osm, Color.BLACK);


        paint.setStrokeWidth(getOsmView().res.toDPf(STROKE_WIDTH));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }




    @Override
    public void draw(MapPainter painter) {
        new TrackPainter(painter).walkTrack(getGpxList());
    }


    private class TrackPainter extends GpxListPainter {
        private final MapPainter painter;

        public TrackPainter(MapPainter p) {
            super(p, getOsmView().res);
            painter = p;
        }

        
        @Override
        public void drawEdge(MapTwoNodes nodes) {
            painter.canvas.drawEdge(nodes, paint);
        }

 

        
        @Override
        public void drawNode(PixelNode node) {
            int altitude=node.point.getAltitude();
            int color=ColorTable.altitude.getColor(altitude);
            paint.setColor(color);
        }
    }
}
