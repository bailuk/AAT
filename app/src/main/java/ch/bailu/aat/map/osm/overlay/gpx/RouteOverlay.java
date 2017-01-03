package ch.bailu.aat.map.osm.overlay.gpx;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.services.dem.ElevationProvider;
import ch.bailu.aat.views.graph.ColorTable;
import ch.bailu.aat.map.osm.AbsOsmView;
import ch.bailu.aat.map.osm.overlay.MapPainter;
import ch.bailu.aat.map.osm.overlay.MapTwoNodes;
import ch.bailu.aat.map.osm.overlay.MapTwoNodes.PixelNode;


public class RouteOverlay extends GpxOverlay {

    
    public RouteOverlay(AbsOsmView o) {
        this(o,  -1);
    }


    public RouteOverlay(AbsOsmView o, int color) {
        super(o, toColor(color));
    }


    private static int toColor(int c) {
        if (c < 0) return AppTheme.getHighlightColor3();
        return c;
    }

    @Override
    public void draw(MapPainter painter) {
        new RoutePainter(painter).walkTrack(getGpxList());
    }


    private class RoutePainter extends GpxListPainter {

        private final MapPainter painter;

        public RoutePainter(MapPainter p) {
            super(p, getOsmView().res);
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
