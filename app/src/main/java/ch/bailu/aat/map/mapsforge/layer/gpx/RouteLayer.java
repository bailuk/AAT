package ch.bailu.aat.map.mapsforge.layer.gpx;

import android.content.SharedPreferences;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;
import ch.bailu.aat.map.mapsforge.layer.context.MapContextTwoNodes;
import ch.bailu.aat.services.dem.ElevationProvider;
import ch.bailu.aat.views.graph.ColorTable;

public class RouteLayer extends GpxLayer {

    private final MapContext mcontext;
    public RouteLayer(MapContext o) {
        this(o,  -1);
    }


    public RouteLayer(MapContext o, int color) {
        super(toColor(color));
        mcontext = o;
    }


    private static int toColor(int c) {
        if (c < 0) return AppTheme.getHighlightColor3();
        return c;
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        new RoutePainter().walkTrack(getGpxList());
    }


    private class RoutePainter extends GpxListPainter {



        public RoutePainter() {

            super(mcontext);
        }


        @Override
        public void drawEdge(MapContextTwoNodes nodes) {
            mcontext.draw.edge(nodes);
        }


        @Override
        public void drawNode(MapContextTwoNodes.PixelNode node) {
            int c;
            int altitude=node.point.getAltitude();

            if (altitude == ElevationProvider.NULL_ALTITUDE) c=getColor();
            else c= ColorTable.altitude.getColor(altitude);

            mcontext.draw.bitmap(mcontext.draw.nodeBitmap.getTileBitmap(), node.pixel, c);
        }
    }
}
