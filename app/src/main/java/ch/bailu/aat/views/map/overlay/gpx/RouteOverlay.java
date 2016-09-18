package ch.bailu.aat.views.map.overlay.gpx;

import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.dem.ElevationProvider;
import ch.bailu.aat.views.graph.ColorTable;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.MapTwoNodes;
import ch.bailu.aat.views.map.overlay.MapTwoNodes.PixelNode;


public class RouteOverlay extends GpxOverlay {

    
    public RouteOverlay(AbsOsmView osmPreview, int id) {
        this(osmPreview, id,  AppTheme.getHighlightColor3());
    }


    public RouteOverlay(AbsOsmView osmPreview, int id, int color) {
        super(osmPreview, id, color);
    }


    @Override
    public void draw(MapPainter painter) {
        new RoutePainter(painter).walkTrack(getGpxList());
    }


    private class RoutePainter extends GpxListPainter {

        private final MapPainter painter;

        public RoutePainter(MapPainter p) {
            super(p);
            painter = p;
        }


        @Override
        public void drawEdge(MapTwoNodes nodes) {
            painter.canvas.drawEdge(nodes);
        }

        
         @Override
        public void drawNode(PixelNode node) {
             int c;
             int altitude=node.point.getAltitude();
             
             if (altitude == ElevationProvider.NULL_ALTITUDE) c=getColor();
             else c=ColorTable.altitude.getColor(altitude);

             
             painter.canvas.draw(painter.nodeBitmap, node.pixel, c);
        }		
    }
}
