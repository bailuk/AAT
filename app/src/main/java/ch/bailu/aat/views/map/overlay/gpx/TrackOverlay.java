package ch.bailu.aat.views.map.overlay.gpx;

import android.graphics.Color;
import android.graphics.Paint;
import ch.bailu.aat.views.graph.ColorTable;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.MapTwoNodes;
import ch.bailu.aat.views.map.overlay.MapTwoNodes.PixelNode;

public class TrackOverlay extends GpxOverlay{
    private static int STROKE_WIDTH=5;
    private final Paint paint=new Paint();



    public TrackOverlay(AbsOsmView osmPreview, int id) {
        super(osmPreview, id, Color.BLACK);
        
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }




    @Override
    public void draw(MapPainter painter) {
        paint.setStrokeWidth(Math.max(STROKE_WIDTH, 
                painter.projection.getPixelFromDistance(5)));
        new TrackPainter(painter).walkTrack(getGpxList());
    }


    private class TrackPainter extends GpxListPainter {
        private MapPainter painter;

        public TrackPainter(MapPainter p) {
            super(p);
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
